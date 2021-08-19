package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMatchHistory is a Querydsl query type for MatchHistory
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMatchHistory extends EntityPathBase<MatchHistory> {

    private static final long serialVersionUID = -1087605807L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMatchHistory matchHistory = new QMatchHistory("matchHistory");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Integer> total = createNumber("total", Integer.class);

    public final StringPath type = createString("type");

    public final QUser user;

    public final NumberPath<Integer> win = createNumber("win", Integer.class);

    public QMatchHistory(String variable) {
        this(MatchHistory.class, forVariable(variable), INITS);
    }

    public QMatchHistory(Path<? extends MatchHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMatchHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMatchHistory(PathMetadata metadata, PathInits inits) {
        this(MatchHistory.class, metadata, inits);
    }

    public QMatchHistory(Class<? extends MatchHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

