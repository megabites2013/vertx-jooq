/*
 * This file is generated by jOOQ.
*/
package generated.future.guice.vertx.tables.interfaces;


import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface ISomething extends Serializable {

    /**
     * Setter for <code>VERTX.SOMETHING.SOMEID</code>.
     */
    public ISomething setSomeid(Integer value);

    /**
     * Getter for <code>VERTX.SOMETHING.SOMEID</code>.
     */
    public Integer getSomeid();

    /**
     * Setter for <code>VERTX.SOMETHING.SOMESTRING</code>.
     */
    public ISomething setSomestring(String value);

    /**
     * Getter for <code>VERTX.SOMETHING.SOMESTRING</code>.
     */
    public String getSomestring();

    /**
     * Setter for <code>VERTX.SOMETHING.SOMEHUGENUMBER</code>.
     */
    public ISomething setSomehugenumber(Long value);

    /**
     * Getter for <code>VERTX.SOMETHING.SOMEHUGENUMBER</code>.
     */
    public Long getSomehugenumber();

    /**
     * Setter for <code>VERTX.SOMETHING.SOMESMALLNUMBER</code>.
     */
    public ISomething setSomesmallnumber(Short value);

    /**
     * Getter for <code>VERTX.SOMETHING.SOMESMALLNUMBER</code>.
     */
    public Short getSomesmallnumber();

    /**
     * Setter for <code>VERTX.SOMETHING.SOMEREGULARNUMBER</code>.
     */
    public ISomething setSomeregularnumber(Integer value);

    /**
     * Getter for <code>VERTX.SOMETHING.SOMEREGULARNUMBER</code>.
     */
    public Integer getSomeregularnumber();

    /**
     * Setter for <code>VERTX.SOMETHING.SOMEBOOLEAN</code>.
     */
    public ISomething setSomeboolean(Boolean value);

    /**
     * Getter for <code>VERTX.SOMETHING.SOMEBOOLEAN</code>.
     */
    public Boolean getSomeboolean();

    /**
     * Setter for <code>VERTX.SOMETHING.SOMEDOUBLE</code>.
     */
    public ISomething setSomedouble(Double value);

    /**
     * Getter for <code>VERTX.SOMETHING.SOMEDOUBLE</code>.
     */
    public Double getSomedouble();

    /**
     * Setter for <code>VERTX.SOMETHING.SOMEJSONOBJECT</code>.
     */
    public ISomething setSomejsonobject(JsonObject value);

    /**
     * Getter for <code>VERTX.SOMETHING.SOMEJSONOBJECT</code>.
     */
    public JsonObject getSomejsonobject();

    /**
     * Setter for <code>VERTX.SOMETHING.SOMEJSONARRAY</code>.
     */
    public ISomething setSomejsonarray(JsonArray value);

    /**
     * Getter for <code>VERTX.SOMETHING.SOMEJSONARRAY</code>.
     */
    public JsonArray getSomejsonarray();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common interface ISomething
     */
    public void from(generated.future.guice.vertx.tables.interfaces.ISomething from);

    /**
     * Copy data into another generated Record/POJO implementing the common interface ISomething
     */
    public <E extends generated.future.guice.vertx.tables.interfaces.ISomething> E into(E into);

    default ISomething fromJson(io.vertx.core.json.JsonObject json) {
        setSomeid(json.getInteger("SOMEID"));
        setSomestring(json.getString("SOMESTRING"));
        setSomehugenumber(json.getLong("SOMEHUGENUMBER"));
        setSomesmallnumber(json.getInteger("SOMESMALLNUMBER")==null?null:json.getInteger("SOMESMALLNUMBER").shortValue());
        setSomeregularnumber(json.getInteger("SOMEREGULARNUMBER"));
        setSomeboolean(json.getBoolean("SOMEBOOLEAN"));
        setSomedouble(json.getDouble("SOMEDOUBLE"));
        setSomejsonobject(json.getJsonObject("SOMEJSONOBJECT"));
        setSomejsonarray(json.getJsonArray("SOMEJSONARRAY"));
        return this;
    }


    default io.vertx.core.json.JsonObject toJson() {
        io.vertx.core.json.JsonObject json = new io.vertx.core.json.JsonObject();
        json.put("SOMEID",getSomeid());
        json.put("SOMESTRING",getSomestring());
        json.put("SOMEHUGENUMBER",getSomehugenumber());
        json.put("SOMESMALLNUMBER",getSomesmallnumber());
        json.put("SOMEREGULARNUMBER",getSomeregularnumber());
        json.put("SOMEBOOLEAN",getSomeboolean());
        json.put("SOMEDOUBLE",getSomedouble());
        json.put("SOMEJSONOBJECT",getSomejsonobject());
        json.put("SOMEJSONARRAY",getSomejsonarray());
        return json;
    }

}
