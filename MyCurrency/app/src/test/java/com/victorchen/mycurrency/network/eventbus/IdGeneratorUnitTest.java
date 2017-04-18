package com.victorchen.mycurrency.network.eventbus;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class IdGeneratorUnitTest {

    @Test
    public void test_idGenerator() {
        assertThat(IdGenerator.getInstance(), notNullValue());

        IdGenerator.getInstance().setInitialId(0);
        assertThat(IdGenerator.getInstance().nextId(), equalTo(0));
        assertThat(IdGenerator.getInstance().nextId(), equalTo(1));
        assertThat(IdGenerator.getInstance().nextId(), equalTo(2));
        assertThat(IdGenerator.getInstance().nextId(), equalTo(3));

        for (int i = 0; i < 100; i++) {
            IdGenerator.getInstance().nextId();
        }
        assertThat(IdGenerator.getInstance().nextId(), equalTo(104));

        IdGenerator.getInstance().setInitialId(Integer.MAX_VALUE - 1);
        assertThat(IdGenerator.getInstance().nextId(), equalTo(Integer.MAX_VALUE - 1));
        assertThat(IdGenerator.getInstance().nextId(), equalTo(Integer.MAX_VALUE));
        assertThat(IdGenerator.getInstance().nextId(), equalTo(Integer.MIN_VALUE));
        assertThat(IdGenerator.getInstance().nextId(), equalTo(Integer.MIN_VALUE + 1));
        assertThat(IdGenerator.getInstance().nextId(), equalTo(Integer.MIN_VALUE + 2));
    }

}
