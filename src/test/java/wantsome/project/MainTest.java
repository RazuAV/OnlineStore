package wantsome.project;

import org.junit.Assert;
import org.junit.Test;
import wantsome.online_store.db.products.ProductsDao;

import java.sql.SQLException;

/**
 * Example of tests. Replace this with actual unit tests (like for DB part)
 * (or else remove this class)
 */
public class MainTest {
        @Test
        public void testProductWithDescriptionIsPresent () {
            try {
                Assert.assertTrue(ProductsDao.getProductsByDescription("Fake grass").isPresent());
            }catch (SQLException e){
                throw new RuntimeException("error" + e.getMessage());
            }
    }

    }