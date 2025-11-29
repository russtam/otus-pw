package ru.rustam.otus.order;

import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import ru.rustam.otus.common.model.OrderDto;
import ru.rustam.otus.common.model.OrderItemDto;
import ru.rustam.otus.order.db.OrderEntity;
import ru.rustam.otus.order.db.OrderItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class ConvertUtil {

    public static OrderEntity convertOrder(OrderDto src) {
        OrderEntity dst = new OrderEntity();
        dst.setOrderId(src.getOrderId());
        dst.setUserName(src.getUserName());
        dst.setAmount(src.getAmount());
        dst.setContactPhone(src.getContactPhone());
        dst.setEmail(src.getEmail());
        dst.setDeliveryAddress(src.getDeliveryAddress());
        dst.setItems(convertItemDtoList(src.getItems()));
        dst.setStatus(src.getStatus());
        dst.setPaymentLink(src.getPaymentLink());
        dst.setCreated(src.getCreated());
        return dst;
    }

    public static OrderItem convertItem(OrderItemDto src) {
        return new OrderItem(src.getItemId(), src.getCount());
    }

    public static List<OrderItem> convertItemDtoList(List<OrderItemDto> src) {
        if (src == null) {
            return null;
        }
        if (src.isEmpty()) {
            return Collections.emptyList();
        }
        return src.stream().map(ConvertUtil::convertItem).toList();
    }

    public static OrderDto convertOrder(OrderEntity src) {
        OrderDto dst = new OrderDto();
        dst.setOrderId(src.getOrderId());
        dst.setUserName(src.getUserName());
        dst.setAmount(src.getAmount());
        dst.setContactPhone(src.getContactPhone());
        dst.setEmail(src.getEmail());
        dst.setDeliveryAddress(src.getDeliveryAddress());
        dst.setItems(convertItemList(src.getItems()));
        dst.setStatus(src.getStatus());
        dst.setPaymentLink(src.getPaymentLink());
        dst.setCreated(src.getCreated());
        return dst;
    }

    public static List<OrderDto> convertOrders(List<OrderEntity> src) {
        if (CollectionUtils.isEmpty(src)) {
            return Collections.emptyList();
        }
        List<OrderDto> result = new ArrayList<>(src.size());
        src.forEach(order -> result.add(convertOrder(order)));
        return result;
    }

    public static OrderItemDto convertItem(OrderItem src) {
        return new OrderItemDto(src.getItemId(), src.getCount());
    }

    public static List<OrderItemDto> convertItemList(List<OrderItem> src) {
        if (src == null) {
            return null;
        }
        if (src.isEmpty()) {
            return Collections.emptyList();
        }
        return src.stream().map(ConvertUtil::convertItem).toList();
    }

}
