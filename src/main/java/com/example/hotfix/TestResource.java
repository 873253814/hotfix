package com.example.hotfix;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Resource
public class TestResource {
    private int id;

    private String name;

    private List<Integer> armyIds;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getArmyIds() {
        return armyIds;
    }

    public void setArmyIds(List<Integer> armyIds) {
        this.armyIds = armyIds;
    }
}
