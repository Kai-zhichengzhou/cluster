package com.cluster.exception;

public class ClusterFullException extends RuntimeException{

    public ClusterFullException(String message)
    {
        super(message);
    }
}
