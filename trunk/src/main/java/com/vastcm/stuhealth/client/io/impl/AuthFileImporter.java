/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.io.impl;

import com.vastcm.stuhealth.client.AppContext;
import com.vastcm.stuhealth.client.entity.School;
import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.client.entity.service.ISchoolService;
import com.vastcm.stuhealth.client.entity.service.ISchoolTreeNodeService;
import com.vastcm.stuhealth.client.framework.SystemUtils;
import com.vastcm.stuhealth.client.io.IAuthFileImporter;
import com.vastcm.stuhealth.client.utils.Des;
import com.vastcm.stuhealth.client.utils.ExceptionUtils;
import com.vastcm.stuhealth.client.utils.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.security.auth.login.AppConfigurationEntry;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author house
 */
public class AuthFileImporter implements IAuthFileImporter {
    private Logger logger = LoggerFactory.getLogger(AuthFileImporter.class);
    private final String desKey = "tjsltykj";
    
    private String decodeAuthFile(File f) throws IOException {
    	StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader(f));
		for(String line = ""; line != null; line = br.readLine()) {
			sb.append(line);
		}
		br.close();
		try {
			return URLDecoder.decode(Des.decrypt(sb.toString(), desKey), "UTF-8");
		} 
		catch (Exception e) {
			logger.error("decode auth file error.", e);
			throw new IOException("解析验证文件错误，请确认验证文件下载来源是否可靠或是否被篡改！", e);
		}
    }

    @Override
    public List<School> parse(File f) throws IOException {
        List<School> rs = new ArrayList<School>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder domBuilder = dbf.newDocumentBuilder();
            Document doc = domBuilder.parse(new ByteArrayInputStream(decodeAuthFile(f).getBytes()));
            Element root = doc.getDocumentElement();
            if(root == null) {
                throw new IOException("File Content ERROR!");
            }
            NodeList schoolsList = root.getElementsByTagName("HW_School");
            if(schoolsList == null || schoolsList.getLength() == 0) {
            	IOException ex = new IOException("Parse XML ERROR! Wrong Format of the license.");
            	ExceptionUtils.writeExceptionLog(logger, ex);
                throw ex;
            }
            Element schoolNode = (Element) schoolsList.item(0).getChildNodes();
            String schoolCode = StringUtils.notNull(schoolNode.getAttribute("SchoolCode"));
            
            School school = new School();
            school.setSchoolCode(schoolCode);
            school.setCityId(StringUtils.notNull(schoolNode.getAttribute("CityId")));
            school.setCityName(StringUtils.notNull(schoolNode.getAttribute("CityName")));
            school.setDistrictCode(StringUtils.notNull(schoolNode.getAttribute("DistrictCode")));
            school.setDistrictName(StringUtils.notNull(schoolNode.getAttribute("DistrictName")));
            school.setName(StringUtils.notNull(schoolNode.getAttribute("Name")));
            school.setAddress(StringUtils.notNull(schoolNode.getAttribute("Address")));
            school.setEmail(StringUtils.notNull(schoolNode.getAttribute("Email")));
            school.setLinkMan(StringUtils.notNull(schoolNode.getAttribute("LinkMan")));
            school.setMobilePhone(StringUtils.notNull(schoolNode.getAttribute("MobilePhone")));
            school.setOfficePhone(StringUtils.notNull(schoolNode.getAttribute("OfficePhone")));
            school.setStatus(StringUtils.notNull(schoolNode.getAttribute("State")));
            school.setSchoolCharacter(StringUtils.notNull(schoolNode.getAttribute("SchoolCharacter")));
            school.setSchoolType(StringUtils.notNull(schoolNode.getAttribute("SchoolType")));
            school.setHostSchoolCode(StringUtils.notNull(schoolNode.getAttribute("HostSchool")));
            school.setSecCode(StringUtils.notNull(schoolNode.getAttribute("Sign")));
            rs.add(school);
            
            NodeList mergedSchools = schoolNode.getElementsByTagName("MergedSchool");
            if(mergedSchools != null && mergedSchools.getLength() > 0) {
            	for(int i = 0; i < mergedSchools.getLength(); i++) {
            		Element mergedSchool = (Element) mergedSchools.item(i);
            		School s = new School();
            		s.setSchoolCode(StringUtils.notNull(mergedSchool.getAttribute("SchoolCode")));
            		s.setHostSchoolCode(StringUtils.notNull(mergedSchool.getAttribute("HostSchoolCode")));
            		rs.add(s);
            	}
            }
            
            return rs;
        } 
        catch (ParserConfigurationException ex) {
            ExceptionUtils.writeExceptionLog(logger, ex);
            throw new IOException("Parse XML ERROR!", ex);
        } 
        catch (SAXException ex) {
            ExceptionUtils.writeExceptionLog(logger, ex);
            throw new IOException("Parse XML ERROR!", ex);
        }
        catch (Exception ex) {
            ExceptionUtils.writeExceptionLog(logger, ex);
            throw new IOException(ex);
        }
        
    }

    
}
