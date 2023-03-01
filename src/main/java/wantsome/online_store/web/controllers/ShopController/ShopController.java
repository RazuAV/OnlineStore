package wantsome.online_store.web.controllers.ShopController;

import io.javalin.http.Context;
import wantsome.online_store.db.products.ProductType;
import wantsome.online_store.db.products.ProductsDao;
import wantsome.online_store.db.products.ProductsDto;

import java.sql.SQLException;
import java.util.*;

public class ShopController {


    public static void showShopPage(Context ctx) {
        renderShopPage(ctx);
    }

    public static void renderShopPage(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        ctx.render("/secure/shoppage.vm", model);
    }

    public static void handleSearchRequests(Context ctx) {
        String category = ctx.formParam("category");
        String description = ctx.formParam("description");
        Map<String, Object> model = new HashMap<>();

        try {

            if (description != null && !description.isEmpty()) {
                List<ProductsDto> products = new ArrayList<>();
                Optional<ProductsDto> productsDtoOptional = ProductsDao.getProductsByDescription(description);
                productsDtoOptional.ifPresent(products::add);
                model.put("listItems", products);
            }

            else if (category != null && !category.equals("None")) {
                model.put("listItems", ProductsDao.getProductsByProductType(ProductType.valueOf(category)));
            }

            else {
                model.put("listItems", ProductsDao.getAllProducts());
            }
            model.put("category", category);
            model.put("description", description);
        } catch (SQLException e) {
            throw new RuntimeException("Error listing list of products " + " " + e.getMessage());
        }
        ctx.render("/secure/shoppage.vm", model);
    }
}
