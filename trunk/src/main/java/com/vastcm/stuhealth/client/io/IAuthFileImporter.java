/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.io;

import com.vastcm.stuhealth.client.entity.School;
import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author House
 */
public interface IAuthFileImporter {
    public List<School> parse(File f) throws IOException;
}
