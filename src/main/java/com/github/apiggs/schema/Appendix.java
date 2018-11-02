package com.github.apiggs.schema;

import com.github.apiggs.util.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * 附录
 */
public class Appendix {

    String name;
    List<Cell<String>> cells = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Cell<String>> getCells() {
        return cells;
    }
}
