package com.googlecode.mad_schuelerturnier.business.dataloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Database Dump and Load
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.5
 */
@Component
public class DatabaseDumpAndImport {

    private static final Logger LOG = Logger.getLogger(CVSSpielParser.class);

    @Autowired
    private org.springframework.jdbc.datasource.DriverManagerDataSource datasource;


    public void fullDatabaseExportToRoot(){
        fullDatabaseExport("/test.xml");
    }

    public void fullDatabaseImportToRoot(){
        fullDatabaseImport("/test.xml");
    }

    public void fullDatabaseExport(String file) {
        File f = new File(file);
        try{
            IDatabaseConnection connection = new DatabaseConnection(datasource.getConnection());
            ITableFilter filter = new DatabaseSequenceFilter(connection);
            IDataSet dataset    = new FilteredDataSet(filter, connection.createDataSet());
            FlatXmlDataSet.write(dataset, new FileOutputStream(f));
        } catch (Exception e){
           LOG.error(e.getMessage(),e);
        }
    }

    public void fullDatabaseImport(String file) {
        try{
            File f = new File(file);
            IDataSet dataSet = new FlatXmlDataSet(f, true);
            IDatabaseConnection connection = new DatabaseConnection(datasource.getConnection());
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
        } catch (Exception e){
            LOG.error(e.getMessage(),e);
        }
    }


}