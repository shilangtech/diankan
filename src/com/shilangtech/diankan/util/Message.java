package com.shilangtech.diankan.util;

import java.util.ArrayList;

/**
 * @author google_acmer
 *
 */
public class Message {
	
public String text;  //������Ϣ
public String record_path;  //¼������·��
public ArrayList<String> pic_path; //ͼƬλ��·��
public boolean IsPublic; //�Ƿ�Ϊ������Ϣ

public Message(){
	text=null;
	record_path=null;
	pic_path=null;
	IsPublic=true;
}

/**
 * ���캯��
 * @param text
 * @param record_path
 * @param pic_path
 * @param IsPublic
 */
public Message(String text,String record_path,ArrayList<String> pic_path,boolean IsPublic){
	this.text=text;
	this.record_path=record_path;
	this.pic_path=pic_path;
	this.IsPublic=IsPublic;
}

/**
 * @return �õ�Message����
 */
public Message getMessage(){
	return this;
}

public String getText() {
	return text;
}
public void setText(String text) {
	this.text = text;
}
public String getRecord_path() {
	return record_path;
}
public void setRecord_path(String record_path) {
	this.record_path = record_path;
}
public ArrayList<String> getPic_path() {
	return pic_path;
}
public void setPic_path(ArrayList<String> pic_path) {
	this.pic_path = pic_path;
}
public boolean isIsPublic() {
	return IsPublic;
}
public void setIsPublic(boolean isPublic) {
	IsPublic = isPublic;
}
public void CleanMessage(){
	text=null;
	record_path=null;
	pic_path.clear();
	IsPublic=true;
}


}
