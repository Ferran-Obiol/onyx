package libhidapi;

import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Field;
import org.bridj.ann.Library;

/**
 * hidapi info structure<br>
 * <i>native declaration : /usr/include/wchar.h:19</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a
 * href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a
 * href="http://bridj.googlecode.com/">BridJ</a> .
 */
@Library("hidapi")
public class hid_device_info extends StructObject {
  public hid_device_info() {
    super();
  }

  public hid_device_info(Pointer pointer) {
    super(pointer);
  }

  /**
   * Platform-specific device path<br>
   * C type : char*
   */
  @Field(0)
  public Pointer<Byte> path() {
    return this.io.getPointerField(this, 0);
  }

  /**
   * Platform-specific device path<br>
   * C type : char*
   */
  @Field(0)
  public hid_device_info path(Pointer<Byte> path) {
    this.io.setPointerField(this, 0, path);
    return this;
  }

  // / Device Vendor ID
  @Field(1)
  public short vendor_id() {
    return this.io.getShortField(this, 1);
  }

  // / Device Vendor ID
  @Field(1)
  public hid_device_info vendor_id(short vendor_id) {
    this.io.setShortField(this, 1, vendor_id);
    return this;
  }

  // / Device Product ID
  @Field(2)
  public short product_id() {
    return this.io.getShortField(this, 2);
  }

  // / Device Product ID
  @Field(2)
  public hid_device_info product_id(short product_id) {
    this.io.setShortField(this, 2, product_id);
    return this;
  }

  /**
   * Serial Number<br>
   * C type : wchar_t*
   */
  @Field(3)
  public Pointer<Character> serial_number() {
    return this.io.getPointerField(this, 3);
  }

  /**
   * Serial Number<br>
   * C type : wchar_t*
   */
  @Field(3)
  public hid_device_info serial_number(Pointer<Character> serial_number) {
    this.io.setPointerField(this, 3, serial_number);
    return this;
  }

  /**
   * Device Release Number in binary-coded decimal,<br>
   * also known as Device Version Number
   */
  @Field(4)
  public short release_number() {
    return this.io.getShortField(this, 4);
  }

  /**
   * Device Release Number in binary-coded decimal,<br>
   * also known as Device Version Number
   */
  @Field(4)
  public hid_device_info release_number(short release_number) {
    this.io.setShortField(this, 4, release_number);
    return this;
  }

  /**
   * Manufacturer String<br>
   * C type : wchar_t*
   */
  @Field(5)
  public Pointer<Character> manufacturer_string() {
    return this.io.getPointerField(this, 5);
  }

  /**
   * Manufacturer String<br>
   * C type : wchar_t*
   */
  @Field(5)
  public hid_device_info manufacturer_string(Pointer<Character> manufacturer_string) {
    this.io.setPointerField(this, 5, manufacturer_string);
    return this;
  }

  /**
   * Product string<br>
   * C type : wchar_t*
   */
  @Field(6)
  public Pointer<Character> product_string() {
    return this.io.getPointerField(this, 6);
  }

  /**
   * Product string<br>
   * C type : wchar_t*
   */
  @Field(6)
  public hid_device_info product_string(Pointer<Character> product_string) {
    this.io.setPointerField(this, 6, product_string);
    return this;
  }

  /**
   * Usage Page for this Device/Interface<br>
   * (Windows/Mac only).
   */
  @Field(7)
  public short usage_page() {
    return this.io.getShortField(this, 7);
  }

  /**
   * Usage Page for this Device/Interface<br>
   * (Windows/Mac only).
   */
  @Field(7)
  public hid_device_info usage_page(short usage_page) {
    this.io.setShortField(this, 7, usage_page);
    return this;
  }

  /**
   * Usage for this Device/Interface<br>
   * (Windows/Mac only).
   */
  @Field(8)
  public short usage() {
    return this.io.getShortField(this, 8);
  }

  /**
   * Usage for this Device/Interface<br>
   * (Windows/Mac only).
   */
  @Field(8)
  public hid_device_info usage(short usage) {
    this.io.setShortField(this, 8, usage);
    return this;
  }

  /**
   * The USB interface which this logical device<br>
   * represents. Valid on both Linux implementations<br>
   * in all cases, and valid on the Windows implementation<br>
   * only if the device contains more than one interface.
   */
  @Field(9)
  public int interface_number() {
    return this.io.getIntField(this, 9);
  }

  /**
   * The USB interface which this logical device<br>
   * represents. Valid on both Linux implementations<br>
   * in all cases, and valid on the Windows implementation<br>
   * only if the device contains more than one interface.
   */
  @Field(9)
  public hid_device_info interface_number(int interface_number) {
    this.io.setIntField(this, 9, interface_number);
    return this;
  }

  /**
   * Pointer to the next device<br>
   * C type : hid_device_info*
   */
  @Field(10)
  public Pointer<hid_device_info> next() {
    return this.io.getPointerField(this, 10);
  }

  /**
   * Pointer to the next device<br>
   * C type : hid_device_info*
   */
  @Field(10)
  public hid_device_info next(Pointer<hid_device_info> next) {
    this.io.setPointerField(this, 10, next);
    return this;
  }
}
