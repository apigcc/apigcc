package com.github.apiggs.schema;

import java.util.ArrayList;
import java.util.List;

/**
 * 附录
 */
public class Appendix {

    String name;
    List<Cell> cells = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }
}
