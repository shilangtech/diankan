package com.shilangtech.diankan.util;

public class G726DecodeJni {
  public native void g726_Encode(byte[] speech,byte[] bitstream);
  public native void g726_Decode(byte[] bitstream, byte[]speech);
}
