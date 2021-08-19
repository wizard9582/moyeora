package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QInvite is a Querydsl query type for Invite
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInvite extends EntityPathBase<Invite> {

    private static final long serialVersionUID = 1430835531L;

    public static final QInvite invite = new QInvite("invite");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Integer> day = createNumber("day", Integer.class);

    public final StringPath fromUser = createString("fromUser");

    public final NumberPath<Integer> hour = createNumber("hour", Integer.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Integer> min = createNumber("min", Integer.class);

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final StringPath toUser = createString("toUser");

    public final StringPath url = createString("url");

    public QInvite(String variable) {
        super(Invite.class, forVariable(variable));
    }

    public QInvite(Path<? extends Invite> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInvite(PathMetadata metadata) {
        super(Invite.class, metadata);
    }

}

