package io.github.jklingsporn.vertx.jooq.generate.rx.async.guice;

import generated.rx.async.guice.Tables;
import generated.rx.async.guice.tables.daos.SomethingwithoutjsonDao;
import generated.rx.async.guice.tables.pojos.Somethingwithoutjson;
import io.github.jklingsporn.vertx.jooq.generate.AsyncDatabaseConfigurationProvider;
import io.github.jklingsporn.vertx.jooq.generate.rx.RXTestBase;
import io.github.jklingsporn.vertx.jooq.generate.rx.async.AsyncRXDatabaseClientProvider;
import org.jooq.Condition;
import org.junit.BeforeClass;

import java.util.Random;

/**
 * Created by jensklingsporn on 02.11.16.
 */
public class SomethingWithoutJsonDaoTest extends RXTestBase<Somethingwithoutjson, Integer, String, SomethingwithoutjsonDao> {

    public SomethingWithoutJsonDaoTest() {
        super(Tables.SOMETHINGWITHOUTJSON.SOMESTRING, new SomethingwithoutjsonDao(AsyncDatabaseConfigurationProvider.getInstance().createDAOConfiguration(), AsyncRXDatabaseClientProvider.getInstance().getClient()));
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        AsyncDatabaseConfigurationProvider.getInstance().setupDatabase();
    }

    @Override
    protected Somethingwithoutjson create() {
        return createWithId().setSomeid(null);
    }

    @Override
    protected Somethingwithoutjson createWithId() {
        Random random = new Random();
        Somethingwithoutjson something = new Somethingwithoutjson();
        something.setSomeid(random.nextInt());
        something.setSomestring("my_string " + random.nextInt());
        return something;
    }

    @Override
    protected Somethingwithoutjson setId(Somethingwithoutjson pojo, Integer id) {
        return pojo.setSomeid(id);
    }

    @Override
    protected Somethingwithoutjson setSomeO(Somethingwithoutjson pojo, String someO) {
        return pojo.setSomestring(someO);
    }

    @Override
    protected Integer getId(Somethingwithoutjson pojo) {
        return pojo.getSomeid();
    }

    @Override
    protected String createSomeO() {
        return "asdf";
    }

    @Override
    protected Condition eqPrimaryKey(Integer id) {
        return Tables.SOMETHINGWITHOUTJSON.SOMEID.eq(id);
    }

    @Override
    protected void assertDuplicateKeyException(Throwable x) {
        assertException(com.github.mauricio.async.db.mysql.exceptions.MySQLException.class, x);
    }
}
