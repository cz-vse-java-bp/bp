package cz.vse.java.util.persistance.service;


import cz.vse.java.util.database.EDBUse;
import cz.vse.java.util.persistance.entities.EProductStamp;
import cz.vse.java.util.persistance.entities.EUnit;
import cz.vse.java.util.persistance.entities.IEntity;
import cz.vse.java.util.persistance.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ProductService} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 08. 04. 2020
 *
 *
 * @see cz.vse.java.utils.persistance.service
 */
public class ProductService extends AEntityService implements IPersistor {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ProductService class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ProductService() {

        super(EDBUse.STORAGE_MANAGEMENT);
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Saves the {@link IEntity} to the database. First, it checks if
     * the instance is not already there. When it is, it's just updated;
     * otherwise the new creation is done.</p>
     *
     * @param entity to be persisted
     * @throws SQLException when something goes wrong
     */
    @Override
    public void persist(IEntity entity) throws SQLException {

        if(entity instanceof Product) {

            Product p = (Product) entity;
            Connection connection = this.getConnection().getConnection();
            connection.setAutoCommit(false);

            long id = 0;

            ResultSet rs = connection.prepareStatement("SELECT PRODUCT_SEQ.NEXTVAL").executeQuery();

            if(rs.next()) {

                id = rs.getLong(1);
            }


            String query = "INSERT INTO PRODUCT VALUES " +
                    "(" + id + ", ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, p.getBarcode());
            ps.setString(2, p.getProductName());
            ps.setString(3, p.getShortDesc());
            ps.setString(4, p.getLongDesc());
            ps.setBigDecimal(5, p.getPrice());
            ps.setInt(6, p.getQuantity());
            ps.setLong(7, p.getUnit().getId());
            ps.setString(8, p.getLocation());

            ps.execute();

            for (EProductStamp stamp : p.getStamps()) {

                query = "INSERT INTO PRODUCT_TO_PRODUCT_STAMP " +
                        "VALUES (P2PS_SEQ.NEXTVAL, ?, ?)";

                ps = connection.prepareStatement(query);

                ps.setLong(1, id);
                ps.setLong(2, stamp.getId());

                ps.execute();
            }

            connection.commit();
        }
    }

    /**
     * <p>Checks the existence of the {@link IEntity} in the database.</p>
     *
     * @param entity to be checked.
     * @return result of the test
     * @throws SQLException when something goes wrong.
     */
    @Override
    public boolean exists(IEntity entity) throws SQLException {

        if(entity instanceof Product) {

            Connection connection = this.getConnection().getConnection();
            connection.setAutoCommit(false);

            String query = "SELECT ID FROM PRODUCT WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, ((Product) entity).getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {

                connection.commit();
                return true;

            } else {

                connection.commit();
                return false;
            }
        }
        else {
            throw new UnsupportedOperationException("Not supported entity of class "
                    + entity.getClass().getName() + " for " + this.getClass().getName());
        }
    }

    /**
     * <p>Returns all {@link IEntity} instances from the
     * given database.</p>
     *
     * @return all entities from the database
     */
    @Override
    public List<IEntity> getAll() throws SQLException {

        ArrayList<IEntity> entities = new ArrayList<>();

        Connection connection = this
                .getConnection()
                .getConnection();
        connection.setAutoCommit(false);

        String query =
                "SELECT * FROM PRODUCT";

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {

            Product p = new Product();
            p.setId(resultSet.getLong(1));
            p.setBarcode(resultSet.getString(2));
            p.setProductName(resultSet.getString(3));
            p.setShortDesc(resultSet.getString(4));
            p.setLongDesc(resultSet.getString(5));
            p.setPrice(resultSet.getBigDecimal(6));
            p.setQuantity(resultSet.getInt(7));
            p.setUnit(EUnit.getById(resultSet.getLong(8)));
            p.setLocation(resultSet.getString(9));

            String subQuery = "SELECT PRODUCT_STAMP.ID FROM PRODUCT_TO_PRODUCT_STAMP " +
                    "JOIN PRODUCT_STAMP ON PRODUCT_STAMP_ID = PRODUCT_STAMP.ID " +
                    "WHERE PRODUCT_ID = ?";

            PreparedStatement ps2 = connection.prepareStatement(subQuery);
            ps2.setLong(1, resultSet.getLong(1));

            ResultSet rs2 = ps2.executeQuery();

            while(rs2.next()) {

                p.addStamp(EProductStamp.getById(rs2.getLong(1)));
            }

            entities.add(p);
        }

        connection.commit();
        return entities;
    }

    /**
     * <p>Returns the {@link IEntity} with this id.</p>
     *
     * @param id {@link Long} id the entity has.
     * @return {@link IEntity} with given ID.
     */
    @Override
    public IEntity get(Long id) throws SQLException {

        Connection connection = this.getConnection().getConnection();

        connection.setAutoCommit(false);

        String query = "SELECT * FROM PRODUCT WHERE ID = ?";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setLong(1, id);

        ResultSet resultSet = ps.executeQuery();

        Product product = new Product();

        if(resultSet.next()) {

            product.setId(resultSet.getLong(1));
            product.setBarcode(resultSet.getString(2));
            product.setProductName(resultSet.getString(3));
            product.setShortDesc(resultSet.getString(4));
            product.setLongDesc(resultSet.getString(5));
            product.setPrice(resultSet.getBigDecimal(6));
            product.setQuantity(resultSet.getInt(7));
            product.setUnit(EUnit.getById(resultSet.getLong(8)));
            product.setLocation(resultSet.getString(9));


            query = "SELECT NAME FROM PRODUCT_TO_PRODUCT_STAMP " +
                    "JOIN PRODUCT_STAMP ON PRODUCT_TO_PRODUCT_STAMP.PRODUCT_STAMP_ID = PRODUCT_STAMP.ID " +
                    "WHERE PRODUCT_ID = ?;";
            ps = connection.prepareStatement(query);
            ps.setLong(1, product.getId());

            resultSet = ps.executeQuery();

            while (resultSet.next()) {

                product.addStamp(EProductStamp.getStamp(resultSet.getString(1)));
            }
        }

        connection.commit();

        if(product.getId() != null) {

            return product;
        } else {

            LOG.log(Level.SEVERE, "No such product with ID = " + id + " found!");
        }

        return null;
    }

    /**
     * <p>Updates the given {@link IEntity} in the database.</p>
     *
     * @param entity entity to be updated in the database.
     */
    @Override
    public void update(IEntity entity) throws SQLException {

        if(entity instanceof Product) {

            if(this.exists(entity)) {

                Product p = (Product) entity;
                Connection connection = this.getConnection().getConnection();
                connection.setAutoCommit(false);

                String query =
                        "DELETE " +
                        "FROM PRODUCT_TO_PRODUCT_STAMP " +
                        "WHERE PRODUCT_ID = ?";

                PreparedStatement ps = connection.prepareStatement(query);
                ps.setLong(1, p.getId());
                ps.execute();

                query =
                        "UPDATE PRODUCT SET " +
                        "BARCODE = ?, " +
                        "NAME = ?, " +
                        "SHORT_DESC = ?, " +
                        "LONG_DESC = ?, " +
                        "PRICE = ?, " +
                        "QUANTITY = ?, " +
                        "UNIT_ID = ?, " +
                        "LOCATION = ? " +
                        "WHERE ID = ? ";

                ps = connection.prepareStatement(query);
                ps.setString(1, p.getBarcode());
                ps.setString(2, p.getProductName());
                ps.setString(3, p.getShortDesc());
                ps.setString(4, p.getLongDesc());
                ps.setBigDecimal(5, p.getPrice());
                ps.setInt(6, p.getQuantity());
                ps.setLong(7, p.getUnit().getId());
                ps.setString(8, p.getLocation());
                ps.setLong(9, p.getId());

                ps.execute();

                for (EProductStamp stamp : p.getStamps()) {

                    query = "INSERT INTO PRODUCT_TO_PRODUCT_STAMP " +
                            "VALUES (P2PS_SEQ.NEXTVAL, ?, ?)";

                    ps = connection.prepareStatement(query);
                    ps.setLong(1, p.getId());
                    ps.setLong(2, stamp.getId());

                    ps.execute();
                }

                connection.commit();

            } else {

                this.persist(entity);
            }

        } else {

            throw new UnsupportedOperationException("Unsupported entity!");
        }
    }

    /**
     * <p>Removes the {@link IEntity} from the database.</p>
     *
     * @param entity {@link IEntity} to be removed
     * @throws SQLException when there is any error.
     */
    @Override
    public void delete(IEntity entity) throws SQLException {

        if(entity instanceof Product) {

            if(this.exists(entity)) {

                Product p = (Product) entity;
                Connection connection = this.getConnection().getConnection();
                connection.setAutoCommit(false);

                String query =
                        "DELETE FROM PRODUCT_TO_PRODUCT_STAMP " +
                        "WHERE PRODUCT_ID = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setLong(1, p.getId());

                ps.execute();


                query = "DELETE FROM PRODUCT " +
                        "WHERE ID = ?";
                ps = connection.prepareStatement(query);
                ps.setLong(1, p.getId());

                ps.execute();

                connection.commit();
            }

        } else {

            throw new UnsupportedOperationException("Not supported entity!");
        }
    }


    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    public Long getUniqueID() throws SQLException {

        Connection conn = this.getConnection().getConnection();
        String query = "SELECT PRODUCT_SEQ.NEXTVAL";
        PreparedStatement ps = conn.prepareStatement(query);

        ResultSet rs = ps.executeQuery();

        Long id = null;

        if(rs.next()) {

            id = rs.getLong(1);
        }

        return id;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/


}
