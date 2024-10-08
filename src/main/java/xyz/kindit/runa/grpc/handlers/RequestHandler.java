package xyz.kindit.runa.grpc.handlers;

import io.grpc.Status;

public abstract class RequestHandler<B> {

    public final B bean;
    public final Status status = checkRequest();

    protected RequestHandler(B bean) {
        this.bean = bean;
    }

    protected Status checkRequest() { return null; }

}
