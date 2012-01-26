package org.obiba.onyx.jade.instrument.ge.vivid;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.tool.dcmrcv.DicomServer;
import org.dcm4che2.tool.dcmrcv.DicomServer.StoredDicomFile;
import org.obiba.onyx.jade.instrument.InstrumentRunner;
import org.obiba.onyx.jade.instrument.service.InstrumentExecutionService;
import org.obiba.onyx.util.data.Data;
import org.obiba.onyx.util.data.DataBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileSystemUtils;

public class VividInstrumentRunner implements InstrumentRunner {

  protected Logger log = LoggerFactory.getLogger(VividInstrumentRunner.class);

  private InstrumentExecutionService instrumentExecutionService;

  private DicomSettings dicomSettings;

  private File dcmDir;

  private DicomServer server;

  private DicomStorageScp gui;

  public void setInstrumentExecutionService(InstrumentExecutionService instrumentExecutionService) {
    this.instrumentExecutionService = instrumentExecutionService;
  }

  public void setDicomSettings(DicomSettings dicomSettings) {
    this.dicomSettings = dicomSettings;
  }

  @Override
  public void initialize() {
    try {
      File tmpDir = File.createTempFile("dcm", "");
      if(tmpDir.delete() == false || tmpDir.mkdir() == false) {
        throw new RuntimeException("Cannot create temp directory");
      }
      this.dcmDir = tmpDir;
      log.info("DICOM files stored to {}", dcmDir.getAbsolutePath());
    } catch(IOException e) {
      throw new RuntimeException(e);
    }

    this.server = new DicomServer(dcmDir, dicomSettings);
  }

  @Override
  public void run() {
    gui = new DicomStorageScp(server);

    try {
      this.server.start();
    } catch(IOException e) {
      // ignore
    }

    gui.show();
    gui.waitForExit();
  }

  @Override
  public void shutdown() {
    Set<String> output = instrumentExecutionService.getExpectedOutputParameterVendorNames();
    try {
      Map<String, Data> values = new HashMap<String, Data>();

      Vector<Vector<Object>> data = gui.getData();
      for(int i = 0; i < data.size(); i++) {
        Vector<Object> row = data.get(i);
        List<String> columns = DicomStorageScp.columns;
        String studyInstanceUID = (String) row.get(columns.indexOf(DicomStorageScp.STUDYINSTANCEUID));

        List<StoredDicomFile> listDicomFiles = server.listDicomFiles();

        String laterality = (String) row.get(columns.indexOf(DicomStorageScp.LATERALITY));
        if(output.contains("SIDE")) {
          values.put("SIDE", DataBuilder.buildText(laterality));
        }

        int idx = 1;
        for(StoredDicomFile dcm : listDicomFiles) {
          try {
            DicomObject dicomObject = dcm.getDicomObject();
            if(dicomObject.getString(Tag.StudyInstanceUID).equals(studyInstanceUID)) {
              Data dicomData = DataBuilder.buildBinary(dcm.getFile());

              if(dicomObject.contains(Tag.MediaStorageSOPClassUID) && dicomObject.getString(Tag.MediaStorageSOPClassUID).equals("1.2.840.10008.5.1.4.1.1.6.1")) {
                if(output.contains("STILL_IMAGE")) {
                  values.put("STILL_IMAGE", dicomData);
                }
              } else if("SR".equals(dicomObject.getString(Tag.Modality))) {
                if(output.contains("SR")) {
                  values.put("SR", dicomData);
                }
              } else {
                if(output.contains("CINELOOP_" + idx)) {
                  values.put("CINELOOP_" + idx, dicomData);
                }
                idx++;
              }
            }
          } catch(IOException e) {
            log.info(e.getMessage());
          }
        }
        instrumentExecutionService.addOutputParameterValues(values);
      }
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      FileSystemUtils.deleteRecursively(dcmDir);
    }
  }
}