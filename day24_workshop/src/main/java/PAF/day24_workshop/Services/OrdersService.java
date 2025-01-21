package PAF.day24_workshop.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import PAF.day24_workshop.Models.Orders;
import PAF.day24_workshop.Models.OrdersDetails;
import PAF.day24_workshop.Repository.OrdersRepository;

@Service
public class OrdersService {
    @Autowired private OrdersRepository ordersRepository;

    public void addOrder(Orders order){
        ordersRepository.addOrder(order);
    }

    public int getID(){
        return ordersRepository.getID();
    }

    public void delete(){
        ordersRepository.delete();
    }

    public List<Orders> getOrders(){
        return ordersRepository.getOrders();
    }

    public List<OrdersDetails> getAllDetails(int id){
        return ordersRepository.getDetails(id);
    }

    public Boolean insertOrderDetails(OrdersDetails ordersDetails){
        return ordersRepository.insertOrder(ordersDetails);
    }
    
    public double getTotal(int id){
        return ordersRepository.getTotal(id);
    }
}
