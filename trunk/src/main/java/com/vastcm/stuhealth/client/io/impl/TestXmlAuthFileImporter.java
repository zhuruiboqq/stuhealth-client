/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.io.impl;

import com.vastcm.stuhealth.client.entity.School;
import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.client.io.IAuthFileImporter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author House
 */
public class TestXmlAuthFileImporter implements IAuthFileImporter{

    public List<School> parse(File f) throws IOException {
        List<School> ls = new ArrayList<School>();
        SchoolTreeNode s1 = new SchoolTreeNode();
        s1.setCode("D1");
        s1.setName("广东省");
        s1.setParentCode(null);
        s1.setLevel(0);
        s1.setType(SchoolTreeNode.TYPE_DISTRICT);
        s1.setStatus(SchoolTreeNode.STATUS_NORMAL);
        s1.setLongNumber("D1");
//        ls.add(s1);
        
        SchoolTreeNode s2 = new SchoolTreeNode();
        s2.setCode("D2");
        s2.setName("河源市");
        s2.setParentCode("D1");
        s2.setLevel(1);
        s2.setType(SchoolTreeNode.TYPE_DISTRICT);
        s2.setStatus(SchoolTreeNode.STATUS_NORMAL);
        s2.setLongNumber("D1!D2");
//        ls.add(s2);
        
        SchoolTreeNode s3 = new SchoolTreeNode();
        s3.setCode("D3");
        s3.setName("连平县");
        s3.setParentCode("D2");
        s3.setLevel(2);
        s3.setType(SchoolTreeNode.TYPE_DISTRICT);
        s3.setStatus(SchoolTreeNode.STATUS_NORMAL);
        s3.setLongNumber("D1!D2!D3");
//        ls.add(s3);
        
        SchoolTreeNode s4 = new SchoolTreeNode();
        s4.setCode("S1");
        s4.setName("连平县实验中学");
        s4.setParentCode("D3");
        s4.setLevel(3);
        s4.setType(SchoolTreeNode.TYPE_SCHOOL);
        s4.setStatus(SchoolTreeNode.STATUS_NORMAL);
        s4.setLongNumber("D1!D2!D3!S1");
//        ls.add(s4);
        
        return ls;
    }

    public void import2DB() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   

}
