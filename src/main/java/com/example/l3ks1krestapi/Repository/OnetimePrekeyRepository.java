package com.example.l3ks1krestapi.Repository;

import com.example.l3ks1krestapi.Model.OnetimePrekey;
import com.example.l3ks1krestapi.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OnetimePrekeyRepository extends JpaRepository<OnetimePrekey, Integer> {
    List<OnetimePrekey> findAllByUser(User user);
}
