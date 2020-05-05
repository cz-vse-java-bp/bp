package cz.vse.java.utils.persistance.service;

import cz.vse.java.util.database.DBConnection;
import cz.vse.java.util.database.DatabaseConnectionContainer;
import cz.vse.java.util.database.EDBUse;
import cz.vse.java.util.persistance.service.ProductService;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class ProductServiceTest {


    DBConnection dbConnection;


    @Test
    public void getAll() {

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.STORAGE_MANAGEMENT,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );

        ProductService ps = new ProductService();

        try {

            System.out.println("Products:");

            ps.getAll().forEach(n->{

                System.out.println("Entity: " + n.toString());
            });

        } catch (SQLException e) {

            e.printStackTrace();
            fail();
        }
    }
}