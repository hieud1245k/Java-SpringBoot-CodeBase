package com.fcn.fanscoin.blueprint;

import com.fcn.fanscoin.domain.User;
import com.fcn.fanscoin.enums.Gender;
import com.github.javafaker.Faker;
import com.tobedevoured.modelcitizen.annotation.Blueprint;
import com.tobedevoured.modelcitizen.annotation.Default;
import com.tobedevoured.modelcitizen.callback.FieldCallback;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.fcn.fanscoin.constant.Constant.DATE_FORMAT;

@Getter
@Setter
@Blueprint(User.class)
public class UserBlueprint {
    private final Faker faker = new Faker();

    @Default
    private FieldCallback<String> userNo = new FieldCallback<String>() {
        @Override
        public String get(final Object referenceModel) {
            return String.valueOf(faker.idNumber().valid());
        }
    };

    @Default
    private FieldCallback<String> nickname = new FieldCallback<String>() {
        @Override
        public String get(final Object referenceModel) {
            return String.valueOf(faker.name().username());
        }
    };

    @Default
    private Date birthday = new SimpleDateFormat(DATE_FORMAT)
            .parse(new SimpleDateFormat(DATE_FORMAT).format(faker.date().birthday()));

    public UserBlueprint()
            throws ParseException {
    }

    @Default
    private Gender gender = Gender.MALE;

    @Default
    private String bio = faker.lorem().sentence();

    @Default
    private FieldCallback<String> avatar = new FieldCallback<String>() {
        @Override
        public String get(final Object referenceModel) {
            return String.valueOf(faker.avatar().image()).concat(UUID.randomUUID().toString());
        }
    };

    @Default
    private FieldCallback<String> avatarThumbnail = new FieldCallback<String>() {
        @Override
        public String get(final Object referenceModel) {
            return String.valueOf(faker.avatar().image()).concat(UUID.randomUUID().toString());
        }
    };

    @Default
    private FieldCallback<String> coverImage = new FieldCallback<String>() {
        @Override
        public String get(final Object referenceModel) {
            return String.valueOf(faker.avatar().image()).concat(UUID.randomUUID().toString());
        }
    };

    @Default
    private Long numberOfSubscribers = faker.number().randomNumber();

    @Default
    private Long numberOfSubscribings = faker.number().randomNumber();
}
