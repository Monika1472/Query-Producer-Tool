package com.Springpro.Devtools.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="cus_ord")
@ToString
public class CusOrd {
    @Id
    private int Orderid;
    private int Customerid;
    private String Customer;
    private int Rate;
    private String Date;
}