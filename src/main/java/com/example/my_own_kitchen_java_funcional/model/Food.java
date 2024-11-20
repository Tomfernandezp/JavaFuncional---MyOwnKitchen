package com.example.my_own_kitchen_java_funcional.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Food {

    private String view;
    private float price;

    @Builder
    public Food(String view, float price) {
        this.view = view;
        this.price = price;
    }

    public String getView() {
        return view;
    }

    public float getPrice() {
        return price;
    }

    public void setView(String view) {
        this.view = view;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String list(){
        return this.view + " " + this.price + "\n";
    }
}
