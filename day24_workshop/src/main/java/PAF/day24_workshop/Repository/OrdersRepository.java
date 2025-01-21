package PAF.day24_workshop.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import PAF.day24_workshop.Models.Orders;
import PAF.day24_workshop.Models.OrdersDetails;
import PAF.day24_workshop.Utils.SQL;

@Repository
public class OrdersRepository {
    @Autowired
    private JdbcTemplate template;

    public int getID() {
        SqlRowSet rw = template.queryForRowSet(SQL.getAllOrders);
        int id = 0;
        while (rw.next()) {
            id = rw.getInt("order_id");
        }

        return id;
    }

    public void addOrder(Orders order) {
        template.update(SQL.insertOrder, order.getOrder_date(), order.getCustomer_name(), order.getShip_address(),
                order.getNotes(), order.getTax());
    }

    public void delete() {
        int id = getID();
        System.out.printf("To be delete: %d\n", id);
        if (id > 0) {
            template.update(SQL.delete, id);
            template.update(SQL.resetOrder);
        }
    }

    public List<Orders> getOrders() {
        List<Orders> orderList = new LinkedList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        SqlRowSet rs = template.queryForRowSet(SQL.getAllOrders);
        while (rs.next()) {
            Orders orders = new Orders();
            orders.setOrder_id(rs.getInt("order_id"));
            orders.setOrder_date(LocalDate.parse(rs.getString("order_date"), formatter));
            orders.setCustomer_name(rs.getString("customer_name"));
            orders.setNotes(rs.getString("notes"));
            orders.setShip_address(rs.getString("ship_address"));
            orders.setTax(rs.getDouble("tax"));

            orderList.add(orders);
        }

        return orderList;
    }

    public List<OrdersDetails> getDetails(int id) {
        List<OrdersDetails> ordersDetails = new LinkedList<>();
        SqlRowSet rs = template.queryForRowSet(SQL.getAllDetails, id);

        while (rs.next()) {
            OrdersDetails details = new OrdersDetails();
            details.setId(rs.getInt("id"));
            details.setOrder_id(rs.getInt("order_id"));
            details.setProduct(rs.getString("product"));
            details.setUnit_price(rs.getDouble("unit_price"));
            details.setDiscount(rs.getDouble("discount"));
            details.setQuantity(rs.getInt("quantity"));

            ordersDetails.add(details);

        }

        return ordersDetails;
    }

    public Boolean insertOrder(OrdersDetails ordersDetails){
        int checker = template.update(SQL.insertOrderDetails, ordersDetails.getOrder_id(),ordersDetails.getProduct(),ordersDetails.getUnit_price(),ordersDetails.getDiscount(),ordersDetails.getQuantity());

        if(checker > 0){
            return true;
        }
        return false;
    }

    public double getTotal(int id){
        double total = 0;
        SqlRowSet rs = template.queryForRowSet(SQL.getTotal,id);
        while(rs.next()){
            total = rs.getDouble("sum");

        }

        return total;
    }
}
