package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMafia is a Querydsl query type for Mafia
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMafia extends EntityPathBase<Mafia> {

    private static final long serialVersionUID = 465089384L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMafia mafia = new QMafia("mafia");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath role = createString("role");

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final QUserConference userConference;

    public QMafia(String variable) {
        this(Mafia.class, forVariable(variable), INITS);
    }

    public QMafia(Path<? extends Mafia> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMafia(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMafia(PathMetadata metadata, PathInits inits) {
        this(Mafia.class, metadata, inits);
    }

    public QMafia(Class<? extends Mafia> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userConference = inits.isInitialized("userConference") ? new QUserConference(forProperty("userConference"), inits.get("userConference")) : null;
    }

}

