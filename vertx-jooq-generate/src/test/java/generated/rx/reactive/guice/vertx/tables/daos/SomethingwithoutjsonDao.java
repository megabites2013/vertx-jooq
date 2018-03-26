/*
 * This file is generated by jOOQ.
*/
package generated.rx.reactive.guice.vertx.tables.daos;


import generated.rx.reactive.guice.vertx.tables.Somethingwithoutjson;
import generated.rx.reactive.guice.vertx.tables.records.SomethingwithoutjsonRecord;

import io.github.jklingspon.vertx.jooq.shared.reactive.AbstractReactiveVertxDAO;

import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;


import io.reactivex.Single;
import java.util.Optional;
import io.github.jklingsporn.vertx.jooq.rx.reactivepg.ReactiveRXQueryExecutor;
/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@javax.inject.Singleton
public class SomethingwithoutjsonDao extends AbstractReactiveVertxDAO<SomethingwithoutjsonRecord, generated.rx.reactive.guice.vertx.tables.pojos.Somethingwithoutjson, Integer, Single<List<generated.rx.reactive.guice.vertx.tables.pojos.Somethingwithoutjson>>, Single<Optional<generated.rx.reactive.guice.vertx.tables.pojos.Somethingwithoutjson>>, Single<Integer>, Single<Integer>> implements io.github.jklingsporn.vertx.jooq.rx.VertxDAO<SomethingwithoutjsonRecord,generated.rx.reactive.guice.vertx.tables.pojos.Somethingwithoutjson,Integer> {
    @javax.inject.Inject

    /**
     * @param configuration The Configuration used for rendering and query execution.
     * @param vertx the vertx instance
     */
    public SomethingwithoutjsonDao(Configuration configuration, com.julienviet.reactivex.pgclient.PgClient delegate) {
        super(Somethingwithoutjson.SOMETHINGWITHOUTJSON, generated.rx.reactive.guice.vertx.tables.pojos.Somethingwithoutjson.class, new ReactiveRXQueryExecutor<SomethingwithoutjsonRecord,generated.rx.reactive.guice.vertx.tables.pojos.Somethingwithoutjson,Integer>(delegate,generated.rx.reactive.guice.vertx.tables.mappers.RowMappers.getSomethingwithoutjsonMapper()), configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getId(generated.rx.reactive.guice.vertx.tables.pojos.Somethingwithoutjson object) {
        return object.getSomeid();
    }

    /**
     * Find records that have <code>someString IN (values)</code> asynchronously
     */
    public Single<List<generated.rx.reactive.guice.vertx.tables.pojos.Somethingwithoutjson>> findManyBySomestring(List<String> values) {
        return findManyByCondition(Somethingwithoutjson.SOMETHINGWITHOUTJSON.SOMESTRING.in(values));
    }
}
