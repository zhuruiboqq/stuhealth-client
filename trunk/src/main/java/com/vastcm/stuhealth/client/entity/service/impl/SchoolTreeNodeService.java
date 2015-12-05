/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.client.entity.service.ISchoolTreeNodeService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;
import com.vastcm.stuhealth.client.utils.SQLUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author House
 */
public class SchoolTreeNodeService 
    extends CoreService<SchoolTreeNode> implements ISchoolTreeNodeService{

    public List<SchoolTreeNode> getAllNodes() {
        return getSessionFactory().getCurrentSession()
                .createQuery(" FROM SchoolTreeNode node WHERE 1=1 ORDER BY node.longNumber ASC").list();
    }

    public List<SchoolTreeNode> getNormalNodes() {
        return getNodesByStatus("T");
    }

    public List<SchoolTreeNode> getNodesByStatus(String status) {
        return getSessionFactory().getCurrentSession()
                .createQuery(" FROM SchoolTreeNode node WHERE node.status = '" + status + "' ORDER BY node.longNumber ASC ").list();
    }

    public List<SchoolTreeNode> getClassNodes() {
        return getSessionFactory().getCurrentSession()
                .createQuery(" FROM SchoolTreeNode node WHERE node.type = '" + SchoolTreeNode.TYPE_CLASS + "' ORDER BY node.longNumber ASC ").list();
    }

	@Override
	public List<SchoolTreeNode> getClassNodesBySchool(String schoolCode) {
		return getSessionFactory().getCurrentSession()
                .createQuery(" FROM SchoolTreeNode node WHERE node.parentCode = '" + schoolCode + "' AND node.type = '" + SchoolTreeNode.TYPE_CLASS + "' ORDER BY node.longNumber ASC ").list();
	}

	@Override
	public List<SchoolTreeNode> getByCode(String code) {
		return getSessionFactory().getCurrentSession()
                .createQuery(" FROM SchoolTreeNode node WHERE node.code = '" + code + "' ").list();
	}

	@Override
	public void removeClass(String classLongNumber) {
		getSessionFactory().getCurrentSession()
			.createQuery(" DELETE FROM SchoolTreeNode WHERE longNumber = '" + classLongNumber + "' ").executeUpdate();
	}

	@Override
	public void updateStatus(String schoolCode, String status) {
		getSessionFactory().getCurrentSession()
		.createQuery(" UPDATE SchoolTreeNode SET status = '" + status + "' WHERE code = '" + schoolCode + "' ").executeUpdate();
	}

	@Override
	public void saveIfNotExistsByCode(SchoolTreeNode node) {
		List ls = getSessionFactory().getCurrentSession()
					.createQuery(" FROM SchoolTreeNode WHERE code = '" + node.getCode() + "' ").list();
		if(ls.size() == 0) {
			save(node);
		}
	}

	@Override
	public void removeRecursive(String longNumber) {
		getSessionFactory().getCurrentSession()
		.createQuery(" DELETE FROM SchoolTreeNode WHERE longNumber like '" + longNumber + "%' ").executeUpdate();
	}

	@Override
	public void removeByCode(String code) {
		getSessionFactory().getCurrentSession()
		.createQuery(" DELETE FROM SchoolTreeNode WHERE code = '" + code + "' ").executeUpdate();
	}

}
