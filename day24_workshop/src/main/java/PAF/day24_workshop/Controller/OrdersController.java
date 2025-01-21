package PAF.day24_workshop.Controller;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import PAF.day24_workshop.Models.Orders;
import PAF.day24_workshop.Models.OrdersDetails;
import PAF.day24_workshop.Models.exception.LimitException;
import PAF.day24_workshop.Services.OrdersService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping({ "/", "index.html" })
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @GetMapping()
    public ModelAndView start() {
        ModelAndView mav = new ModelAndView("homepage");
        List<Orders> orderList = ordersService.getOrders();
        mav.addObject("orderList", orderList);
        return mav;
    }

    @PostMapping()
    public ModelAndView submitOrder() {
        ModelAndView mav = new ModelAndView("homepage");
        return mav;
    }

    @GetMapping("/order")
    public ModelAndView goToAddPage(HttpSession session) {
        List<OrdersDetails> orders = new LinkedList<>();
        ModelAndView mav = new ModelAndView("orderpage");
        session.setAttribute("orderList", orders);
        return mav;

    }

    @GetMapping("/orderInfo/{id}")
    public ModelAndView getInfo(@PathVariable("id")int id){
        ModelAndView mav = new ModelAndView("orderInfo");
        List<OrdersDetails> allDetails = ordersService.getAllDetails(id);
        mav.addObject("detailsList", allDetails);
        return mav;
    }

    @GetMapping("/reset")
    public ModelAndView reset(HttpSession session) {
        session.invalidate();
        ModelAndView mav = new ModelAndView("orderpage");
        ordersService.delete();
        mav.setViewName("redirect:/order");
        return mav;

    }

    @PostMapping("/addDetails")
    public ModelAndView addDetails(@RequestBody MultiValueMap<String, String> form) {
        ModelAndView mav = new ModelAndView("orderpage");
        Orders order = new Orders();
        LocalDate order_date = LocalDate.parse(form.getFirst("order_date"));
        String name = form.getFirst("customer_name");
        double tax = Double.parseDouble(form.getFirst("tax"));
        String notes = form.getFirst("notes");
        String address = form.getFirst("ship_address");
        order.setCustomer_name(name);
        order.setNotes(notes);
        order.setOrder_date(order_date);
        order.setTax(tax);
        order.setShip_address(address);
        ordersService.addOrder(order);
        mav.addObject("orderInfo",true);

        return mav;
    }

    @PostMapping("/order")
    public ModelAndView addOrder(@RequestBody MultiValueMap<String, String> form, HttpSession session) {
        ModelAndView mav = new ModelAndView("orderpage");
        List<OrdersDetails> orders =(LinkedList) session.getAttribute("orderList");
        if (orders == null) {
            orders = new LinkedList<>();
        }
        String product = form.getFirst("product");
        double unit_price = Double.parseDouble(form.getFirst("unit_price"));
        double discount = Double.parseDouble(form.getFirst("discount"));
        int quantity = Integer.parseInt(form.getFirst("quantity"));
        OrdersDetails ordersDetails = new OrdersDetails();
        ordersDetails.setProduct(product);
        ordersDetails.setUnit_price(unit_price);
        ordersDetails.setDiscount(discount);
        ordersDetails.setQuantity(quantity);

        if (orders.size() > 5) {
            throw new LimitException("The limit of 5 items have been made");
        }
        int id = ordersService.getID();
        ordersDetails.setOrder_id(id);
        orders.add(ordersDetails);
        mav.addObject("orderInfo", true);
        session.setAttribute("orderList", orders);
        mav.addObject("orderList", orders);
        session.setAttribute("id",id);
        return mav;
    }

    @PostMapping("/submit")
    public ModelAndView addOrderDetails(HttpSession session){
        ModelAndView mav = new ModelAndView("submit");
        List<OrdersDetails> orders =(LinkedList) session.getAttribute("orderList");
        for(OrdersDetails ordersDetails: orders){
            ordersService.insertOrderDetails(ordersDetails);
        }
        int id = (int) session.getAttribute("id");
        double total = ordersService.getTotal(id);
        mav.addObject("total", total);

        session.invalidate();
        return mav;
    }


}
