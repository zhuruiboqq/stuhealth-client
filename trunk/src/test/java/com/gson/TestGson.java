package com.gson;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.framework.ResponseContent;

public class TestGson {
	protected static <T> List<T> getListFromJson(ResponseContent exContent, String key, Class<T> clazz) throws Exception {
		String json = exContent.get(key);
		Type typeOfT = new TypeToken<List<T>>() {
		}.getType();
		List<T> list = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(json, typeOfT);
		//		for (T schoolTreeNode : schoolTreeNodeList) {
		//			System.out.println(schoolTreeNode.getName() + " " + schoolTreeNode.getCode());
		//		}
		//		Array.newInstance(clazz, 2);
		return list;
	}

	public static void main(String[] args) throws Exception {
		ResponseContent temp = new ResponseContent();
		temp.set(
				"SchoolTreeNode",
				"[{\"code\":\"2202\",\"name\":\"初中二年级(2)班\",\"parentCode\":\"4408030103008858286\",\"level\":0,\"type\":40,\"status\":\"T\",\"longNumber\":\"440800!440803!4408030103008858286!2202\",\"id\":\"3aa191ba-723e-47c1-b6df-1442f36c920d\"},{\"code\":\"2201\",\"name\":\"初中二年级(1)班\",\"parentCode\":\"4408030103008858286\",\"level\":0,\"type\":40,\"status\":\"T\",\"longNumber\":\"440800!440803!4408030103008858286!2201\",\"id\":\"552e2d2a-b77c-4bdd-bda7-3e35bc91c070\"},{\"code\":\"4408030103008858286\",\"name\":\"湛江市第二十七中学（完中）\",\"parentCode\":\"440803\",\"level\":3,\"type\":30,\"status\":\"T\",\"longNumber\":\"440800!440803!4408030103008858286\",\"id\":\"8f255238-9d78-4a6f-a23e-1ae3c3c31362\"},{\"code\":\"3101\",\"name\":\"高中一年级(1)班\",\"parentCode\":\"4408030103008858286\",\"level\":0,\"type\":40,\"status\":\"T\",\"longNumber\":\"440800!440803!4408030103008858286!3101\",\"id\":\"9cc70cef-9f61-4658-85d4-26502dbe7c63\"},{\"code\":\"2101\",\"name\":\"初中一年级(1)班\",\"parentCode\":\"4408030103008858286\",\"level\":0,\"type\":40,\"status\":\"T\",\"longNumber\":\"440800!440803!4408030103008858286!2101\",\"id\":\"a47223c0-36d1-41c9-8b80-15a9bc11a1ef\"},{\"code\":\"2301\",\"name\":\"初中三年级(1)班\",\"parentCode\":\"4408030103008858286\",\"level\":0,\"type\":40,\"status\":\"T\",\"longNumber\":\"440800!440803!4408030103008858286!2301\",\"id\":\"cd1537d8-36eb-45b6-b62a-24a41e81e55d\"},{\"code\":\"2302\",\"name\":\"初中三年级(2)班\",\"parentCode\":\"4408030103008858286\",\"level\":0,\"type\":40,\"status\":\"T\",\"longNumber\":\"440800!440803!4408030103008858286!2302\",\"id\":\"efd5cbb4-ec81-4ffa-9ea8-27d3f3925033\"},{\"code\":\"2102\",\"name\":\"初中一年级(2)班\",\"parentCode\":\"4408030103008858286\",\"level\":0,\"type\":40,\"status\":\"T\",\"longNumber\":\"440800!440803!4408030103008858286!2102\",\"id\":\"fe1b53e7-d9c7-4c47-8844-3d3f31ca4b4c\"}]");
		String json = temp.get("SchoolTreeNode");
		List<SchoolTreeNode> list = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
				.fromJson(json, new TypeToken<List<SchoolTreeNode>>() {
				}.getType());
		SchoolTreeNode node1 = list.get(0);
		Type typeOfT = new TypeToken<List<SchoolTreeNode>>() {
		}.getType();

		List<SchoolTreeNode> schoolTreeNodeList = getListFromJson(temp, "SchoolTreeNode", SchoolTreeNode.class);//返回的是List<StringMap>
		SchoolTreeNode node2 = schoolTreeNodeList.get(0);
	}
}
