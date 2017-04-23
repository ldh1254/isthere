package com.VO;

import java.util.Date;

/**
 * Created by Administrator on 2017-04-15.
 */

public class Stock {
    private String item_code;
    private String shop_id;
    private Date stock_time;
    private String stock_info;
    private int stock_stock;

    public Stock(String item_code, String shop_id, String stock_info, int stock_stock) {
        this.item_code = item_code;
        this.shop_id = shop_id;
        this.stock_info = stock_info;
        this.stock_stock = stock_stock;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public Date getStock_time() {
        return stock_time;
    }

    public void setStock_time(Date stock_time) {
        this.stock_time = stock_time;
    }

    public String getStock_info() {
        return stock_info;
    }

    public void setStock_info(String stock_info) {
        this.stock_info = stock_info;
    }

    public int getStock_stock() {
        return stock_stock;
    }

    public void setStock_stock(int stock_stock) {
        this.stock_stock = stock_stock;
    }
}
