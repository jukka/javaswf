package org.javaswf.j2avm.model.code;

/**
 * Types of branch condition
 *
 * @author nickmain
 */
public enum BranchType {

    UNCONDITIONAL,
    IF_EQUAL,
    IF_NOT_EQUAL,
    IF_LESS_THAN,
    IF_GREATER_THAN,
    IF_GREATER_OR_EQUAL,
    IF_LESS_OR_EQUAL,
    IF_NULL,
    IF_NOT_NULL,
    IF_SAME_OBJECT,
    IF_NOT_SAME_OBJECT
}
