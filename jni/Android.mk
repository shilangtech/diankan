 # Copyright 2006 The Android Open Source Project
 
  LOCAL_PATH:= $(call my-dir)
  include $(CLEAR_VARS)
  
  LOCAL_SRC_FILES:= \
  		libg726.c \
  		../lib/g711.cpp \
  		../lib/g726_16.cpp \
  		../lib/g726_24.cpp \
  		../lib/g726_32.cpp \
  		../lib/g726_40.cpp \
  		../lib/g726.cpp \
  		../lib/g72x.cpp 
#  		g726.cpp \
#  		G726EnDe.c
LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog
  
  LOCAL_MODULE:= libg726jni
 
  include $(BUILD_SHARED_LIBRARY)
  