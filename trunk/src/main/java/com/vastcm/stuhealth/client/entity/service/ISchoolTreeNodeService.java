/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service;

import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;
import java.util.List;

/**
 *
 * @author House
 */
public interface ISchoolTreeNodeService extends ICoreService<SchoolTreeNode> {
    public List<SchoolTreeNode> getAllNodes();
    public List<SchoolTreeNode> getNormalNodes();
    public List<SchoolTreeNode> getNodesByStatus(String status);
    public List<SchoolTreeNode> getClassNodes();
    public List<SchoolTreeNode> getClassNodesBySchool(String schoolCode);
    public List<SchoolTreeNode> getByCode(String code);
    public void removeByCode(String code);
    public void removeClass(String classLongNumber);
    public void removeRecursive(String longNumber);
    public void updateStatus(String schoolCode, String status);
    public void saveIfNotExistsByCode(SchoolTreeNode node);
}
