package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QConference is a Querydsl query type for Conference
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QConference extends EntityPathBase<Conference> {

    private static final long serialVersionUID = -625543586L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QConference conference = new QConference("conference");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final DateTimePath<java.sql.Timestamp> callEndTime = createDateTime("callEndTime", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> callStartTime = createDateTime("callStartTime", java.sql.Timestamp.class);

    public final QConferenceCategory conferenceCategory;

    public final StringPath description = createString("description");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath isActive = createBoolean("isActive");

    public final QUser ownerId;

    public final StringPath thumbnailUrl = createString("thumbnailUrl");

    public final StringPath title = createString("title");

    public QConference(String variable) {
        this(Conference.class, forVariable(variable), INITS);
    }

    public QConference(Path<? extends Conference> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QConference(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QConference(PathMetadata metadata, PathInits inits) {
        this(Conference.class, metadata, inits);
    }

    public QConference(Class<? extends Conference> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.conferenceCategory = inits.isInitialized("conferenceCategory") ? new QConferenceCategory(forProperty("conferenceCategory")) : null;
        this.ownerId = inits.isInitialized("ownerId") ? new QUser(forProperty("ownerId")) : null;
    }

}

