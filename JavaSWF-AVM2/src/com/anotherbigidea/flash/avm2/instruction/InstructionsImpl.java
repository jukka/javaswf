package com.anotherbigidea.flash.avm2.instruction;

import com.anotherbigidea.flash.avm2.ABC;

/** 
 * A delegating implementation of Instructions
 *
 * @author nickmain
 */
public abstract class InstructionsImpl implements ABC.Instructions {

    private final ABC.Instructions instructions;
    
    /**
     * No delegation
     */
    public InstructionsImpl() {
        instructions = null;
    }
    
    /**
     * @param instructions the delegate target
     */
    public InstructionsImpl( ABC.Instructions instructions ) {
        this.instructions = instructions;
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#add_i() */
    public void add_i() {
        if( instructions != null ) instructions. add_i();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#add() */
    public void add() {
        if( instructions != null ) instructions. add();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#astype(int) */
    public void astype(int name_index) {
        if( instructions != null ) instructions. astype( name_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#astypelate() */
    public void astypelate() {
        if( instructions != null ) instructions. astypelate();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#bitand() */
    public void bitand() {
        if( instructions != null ) instructions. bitand();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#bitnot() */
    public void bitnot() {
        if( instructions != null ) instructions. bitnot();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#bitor() */
    public void bitor() {
        if( instructions != null ) instructions. bitor();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#bitxor() */
    public void bitxor() {
        if( instructions != null ) instructions. bitxor();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#bkpt() */
    public void bkpt() {
        if( instructions != null ) instructions. bkpt();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#bkptline(int) */
    public void bkptline(int line_number) {
        if( instructions != null ) instructions. bkptline( line_number );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#bytecode(byte[]) */
    public void bytecode(byte[] code) {
        if( instructions != null ) instructions. bytecode( code );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#call(int) */
    public void call(int arg_count) {
        if( instructions != null ) instructions. call( arg_count );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#callmethod(int, int) */
    public void callmethod(int disp_id, int arg_count) {
        if( instructions != null ) instructions. callmethod( disp_id, arg_count );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#callproperty(int, int) */
    public void callproperty(int name_index, int arg_count) {
        if( instructions != null ) instructions. callproperty( name_index, arg_count );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#callproplex(int, int) */
    public void callproplex(int name_index, int arg_count) {
        if( instructions != null ) instructions. callproplex( name_index, arg_count );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#callpropvoid(int, int) */
    public void callpropvoid(int name_index, int arg_count) {
        if( instructions != null ) instructions. callpropvoid( name_index, arg_count );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#callstatic(int, int) */
    public void callstatic(int method_id, int arg_count) {
        if( instructions != null ) instructions. callstatic( method_id, arg_count );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#callsuper(int, int) */
    public void callsuper(int name_index, int arg_count) {
        if( instructions != null ) instructions. callsuper( name_index, arg_count );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#callsupervoid(int, int) */
    public void callsupervoid(int name_index, int arg_count) {
        if( instructions != null ) instructions. callsupervoid( name_index, arg_count );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#checkfilter() */
    public void checkfilter() {
        if( instructions != null ) instructions. checkfilter();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#coerce_a() */
    public void coerce_a() {
        if( instructions != null ) instructions. coerce_a();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#coerce_b() */
    public void coerce_b() {
        if( instructions != null ) instructions. coerce_b();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#coerce_d() */
    public void coerce_d() {
        if( instructions != null ) instructions. coerce_d();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#coerce_i() */
    public void coerce_i() {
        if( instructions != null ) instructions. coerce_i();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#coerce_o() */
    public void coerce_o() {
        if( instructions != null ) instructions. coerce_o();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#coerce_s() */
    public void coerce_s() {
        if( instructions != null ) instructions. coerce_s();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#coerce_u() */
    public void coerce_u() {
        if( instructions != null ) instructions. coerce_u();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#coerce(int) */
    public void coerce(int name_index) {
        if( instructions != null ) instructions. coerce( name_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#construct(int) */
    public void construct( int arg_count ) {
        if( instructions != null ) instructions. construct( arg_count );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#constructprop(int, int) */
    public void constructprop(int name_index, int arg_count) {
        if( instructions != null ) instructions. constructprop( name_index, arg_count );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#constructsuper(int) */
    public void constructsuper(int arg_count) {
        if( instructions != null ) instructions. constructsuper( arg_count );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#convert_b() */
    public void convert_b() {
        if( instructions != null ) instructions. convert_b();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#convert_d() */
    public void convert_d() {
        if( instructions != null ) instructions. convert_d();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#convert_i() */
    public void convert_i() {
        if( instructions != null ) instructions. convert_i();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#convert_o() */
    public void convert_o() {
        if( instructions != null ) instructions. convert_o();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#convert_s() */
    public void convert_s() {
        if( instructions != null ) instructions. convert_s();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#convert_u() */
    public void convert_u() {
        if( instructions != null ) instructions. convert_u();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#debug() */
    public void debug() {
        if( instructions != null ) instructions. debug();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#debugfile(int) */
    public void debugfile(int string_index) {
        if( instructions != null ) instructions. debugfile( string_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#debugline(int) */
    public void debugline(int line_number) {
        if( instructions != null ) instructions. debugline( line_number );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#declocal_i(int) */
    public void declocal_i( int register ) {
        if( instructions != null ) instructions. declocal_i( register );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#declocal(int) */
    public void declocal( int register ) {
        if( instructions != null ) instructions. declocal( register );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#decrement_i() */
    public void decrement_i() {
        if( instructions != null ) instructions. decrement_i();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#decrement() */
    public void decrement() {
        if( instructions != null ) instructions. decrement();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#deleteproperty(int) */
    public void deleteproperty(int name_index) {
        if( instructions != null ) instructions. deleteproperty( name_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#divide() */
    public void divide() {
        if( instructions != null ) instructions. divide();
    }

    /** @see org.epistem.io.PipelineInterface#done() */
    public void done() {
        if( instructions != null ) instructions. done();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#dup() */
    public void dup() {
        if( instructions != null ) instructions. dup();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#dxns(int) */
    public void dxns(int string_index) {
        if( instructions != null ) instructions. dxns( string_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#dxnslate() */
    public void dxnslate() {
        if( instructions != null ) instructions. dxnslate();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#equals() */
    public void equals() {
        if( instructions != null ) instructions. equals();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#esc_xattr() */
    public void esc_xattr() {
        if( instructions != null ) instructions. esc_xattr();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#esc_xelem() */
    public void esc_xelem() {
        if( instructions != null ) instructions. esc_xelem();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#finddef(int) */
    public void finddef(int name_index) {
        if( instructions != null ) instructions. finddef( name_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#findproperty(int) */
    public void findproperty(int name_index) {
        if( instructions != null ) instructions. findproperty( name_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#findpropstrict(int) */
    public void findpropstrict(int name_index) {
        if( instructions != null ) instructions. findpropstrict( name_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#getdescendants(int) */
    public void getdescendants(int name_index) {
        if( instructions != null ) instructions. getdescendants( name_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#getglobalscope() */
    public void getglobalscope() {
        if( instructions != null ) instructions. getglobalscope();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#getglobalslot(int) */
    public void getglobalslot(int slot_id) {
        if( instructions != null ) instructions. getglobalslot( slot_id );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#getlex(int) */
    public void getlex(int name_index) {
        if( instructions != null ) instructions. getlex( name_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#getlocal(int) */
    public void getlocal(int register) {
        if( instructions != null ) instructions. getlocal( register );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#getlocal0() */
    public void getlocal0() {
        if( instructions != null ) instructions. getlocal0();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#getlocal1() */
    public void getlocal1() {
        if( instructions != null ) instructions. getlocal1();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#getlocal2() */
    public void getlocal2() {
        if( instructions != null ) instructions. getlocal2();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#getlocal3() */
    public void getlocal3() {
        if( instructions != null ) instructions. getlocal3();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#getproperty(int) */
    public void getproperty(int name_index) {
        if( instructions != null ) instructions. getproperty( name_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#getscopeobject(int) */
    public void getscopeobject(int scope_index) {
        if( instructions != null ) instructions. getscopeobject( scope_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#getslot(int) */
    public void getslot(int slot_id) {
        if( instructions != null ) instructions. getslot( slot_id );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#getsuper(int) */
    public void getsuper(int name_index) {
        if( instructions != null ) instructions. getsuper( name_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#greaterequals() */
    public void greaterequals() {
        if( instructions != null ) instructions. greaterequals();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#greaterthan() */
    public void greaterthan() {
        if( instructions != null ) instructions. greaterthan();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#hasnext() */
    public void hasnext() {
        if( instructions != null ) instructions. hasnext();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#hasnext2(int, int) */
    public void hasnext2(int target_register, int index_register) {
        if( instructions != null ) instructions. hasnext2( target_register, index_register );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#ifeq(int) */
    public void ifeq(int target) {
        if( instructions != null ) instructions. ifeq( target );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#iffalse(int) */
    public void iffalse(int target) {
        if( instructions != null ) instructions. iffalse( target );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#ifge(int) */
    public void ifge(int target) {
        if( instructions != null ) instructions. ifge( target );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#ifgt(int) */
    public void ifgt(int target) {
        if( instructions != null ) instructions. ifgt( target );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#ifle(int) */
    public void ifle(int target) {
        if( instructions != null ) instructions. ifle( target );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#iflt(int) */
    public void iflt(int target) {
        if( instructions != null ) instructions. iflt( target );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#ifne(int) */
    public void ifne(int target) {
        if( instructions != null ) instructions. ifne( target );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#ifnge(int) */
    public void ifnge(int target) {
        if( instructions != null ) instructions. ifnge( target );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#ifngt(int) */
    public void ifngt(int target) {
        if( instructions != null ) instructions. ifngt( target );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#ifnle(int) */
    public void ifnle(int target) {
        if( instructions != null ) instructions. ifnle( target );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#ifnlt(int) */
    public void ifnlt(int target) {
        if( instructions != null ) instructions. ifnlt( target );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#ifstricteq(int) */
    public void ifstricteq(int target) {
        if( instructions != null ) instructions. ifstricteq( target );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#ifstrictne(int) */
    public void ifstrictne(int target) {
        if( instructions != null ) instructions. ifstrictne( target );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#iftrue(int) */
    public void iftrue(int target) {
        if( instructions != null ) instructions. iftrue( target );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#in() */
    public void in() {
        if( instructions != null ) instructions. in();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#inclocal_i(int) */
    public void inclocal_i(int register) {
        if( instructions != null ) instructions. inclocal_i( register );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#inclocal(int) */
    public void inclocal(int register) {
        if( instructions != null ) instructions. inclocal( register );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#increment_i() */
    public void increment_i() {
        if( instructions != null ) instructions. increment_i();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#increment() */
    public void increment() {
        if( instructions != null ) instructions. increment();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#initproperty(int) */
    public void initproperty(int name_index) {
        if( instructions != null ) instructions. initproperty( name_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#instanceof_() */
    public void instanceof_() {
        if( instructions != null ) instructions. instanceof_();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#istype(int) */
    public void istype(int name_index) {
        if( instructions != null ) instructions. istype( name_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#istypelate() */
    public void istypelate() {
        if( instructions != null ) instructions. istypelate();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#jump(int) */
    public void jump(int offset) {
        if( instructions != null ) instructions. jump( offset );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#kill(int) */
    public void kill(int register) {
        if( instructions != null ) instructions. kill( register );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#label() */
    public void label() {
        if( instructions != null ) instructions. label();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#lessequals() */
    public void lessequals() {
        if( instructions != null ) instructions. lessequals();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#lessthan() */
    public void lessthan() {
        if( instructions != null ) instructions. lessthan();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#lookupswitch(int, int[]) */
    public void lookupswitch(int defaultTarget, int[] caseTargets) {
        if( instructions != null ) instructions. lookupswitch( defaultTarget, caseTargets );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#lshift() */
    public void lshift() {
        if( instructions != null ) instructions. lshift();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#modulo() */
    public void modulo() {
        if( instructions != null ) instructions. modulo();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#multiply_i() */
    public void multiply_i() {
        if( instructions != null ) instructions. multiply_i();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#multiply() */
    public void multiply() {
        if( instructions != null ) instructions. multiply();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#negate_i() */
    public void negate_i() {
        if( instructions != null ) instructions. negate_i();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#negate() */
    public void negate() {
        if( instructions != null ) instructions. negate();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#newactivation() */
    public void newactivation() {
        if( instructions != null ) instructions. newactivation();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#newarray(int) */
    public void newarray(int arg_count) {
        if( instructions != null ) instructions. newarray( arg_count );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#newcatch(int) */
    public void newcatch(int catch_index) {
        if( instructions != null ) instructions. newcatch( catch_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#newclass(int) */
    public void newclass(int class_index) {
        if( instructions != null ) instructions. newclass( class_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#newfunction(int) */
    public void newfunction(int method_index) {
        if( instructions != null ) instructions. newfunction( method_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#newobject(int) */
    public void newobject(int key_value_count) {
        if( instructions != null ) instructions. newobject( key_value_count );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#nextname() */
    public void nextname() {
        if( instructions != null ) instructions. nextname();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#nextvalue() */
    public void nextvalue() {
        if( instructions != null ) instructions. nextvalue();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#nop() */
    public void nop() {
        if( instructions != null ) instructions. nop();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#not() */
    public void not() {
        if( instructions != null ) instructions. not();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#pop() */
    public void pop() {
        if( instructions != null ) instructions. pop();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#popscope() */
    public void popscope() {
        if( instructions != null ) instructions. popscope();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#pushbyte(byte) */
    public void pushbyte(byte value) {
        if( instructions != null ) instructions. pushbyte( value );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#pushdouble(int) */
    public void pushdouble(int double_index) {
        if( instructions != null ) instructions. pushdouble( double_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#pushfalse() */
    public void pushfalse() {
        if( instructions != null ) instructions. pushfalse();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#pushint(int) */
    public void pushint(int int_index) {
        if( instructions != null ) instructions. pushint( int_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#pushnamespace(int) */
    public void pushnamespace(int namespace_index) {
        if( instructions != null ) instructions. pushnamespace( namespace_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#pushnan() */
    public void pushnan() {
        if( instructions != null ) instructions. pushnan();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#pushnull() */
    public void pushnull() {
        if( instructions != null ) instructions. pushnull();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#pushscope() */
    public void pushscope() {
        if( instructions != null ) instructions. pushscope();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#pushshort(int) */
    public void pushshort(int value) {
        if( instructions != null ) instructions. pushshort( value );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#pushstring(int) */
    public void pushstring(int string_index) {
        if( instructions != null ) instructions. pushstring( string_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#pushtrue() */
    public void pushtrue() {
        if( instructions != null ) instructions. pushtrue();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#pushuint(int) */
    public void pushuint(int uint_index) {
        if( instructions != null ) instructions. pushuint( uint_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#pushundefined() */
    public void pushundefined() {
        if( instructions != null ) instructions. pushundefined();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#pushwith() */
    public void pushwith() {
        if( instructions != null ) instructions. pushwith();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#returnvalue() */
    public void returnvalue() {
        if( instructions != null ) instructions. returnvalue();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#returnvoid() */
    public void returnvoid() {
        if( instructions != null ) instructions. returnvoid();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#rshift() */
    public void rshift() {
        if( instructions != null ) instructions. rshift();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#setglobalslot(int) */
    public void setglobalslot(int slot_id) {
        if( instructions != null ) instructions. setglobalslot( slot_id );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#setlocal(int) */
    public void setlocal(int register) {
        if( instructions != null ) instructions. setlocal( register );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#setlocal0() */
    public void setlocal0() {
        if( instructions != null ) instructions. setlocal0();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#setlocal1() */
    public void setlocal1() {
        if( instructions != null ) instructions. setlocal1();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#setlocal2() */
    public void setlocal2() {
        if( instructions != null ) instructions. setlocal2();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#setlocal3() */
    public void setlocal3() {
        if( instructions != null ) instructions. setlocal3();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#setproperty(int) */
    public void setproperty(int name_index) {
        if( instructions != null ) instructions. setproperty( name_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#setslot(int) */
    public void setslot(int slot_id) {
        if( instructions != null ) instructions. setslot( slot_id );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#setsuper(int) */
    public void setsuper(int name_index) {
        if( instructions != null ) instructions. setsuper( name_index );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#strictequals() */
    public void strictequals() {
        if( instructions != null ) instructions. strictequals();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#subtract_i() */
    public void subtract_i() {
        if( instructions != null ) instructions. subtract_i();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#subtract() */
    public void subtract() {
        if( instructions != null ) instructions. subtract();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#swap() */
    public void swap() {
        if( instructions != null ) instructions. swap();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#target(int) */
    public void target(int address) {
        if( instructions != null ) instructions. target( address );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#throw_() */
    public void throw_() {
        if( instructions != null ) instructions. throw_();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#timestamp() */
    public void timestamp() {
        if( instructions != null ) instructions. timestamp();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#typeof() */
    public void typeof() {
        if( instructions != null ) instructions. typeof();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.Instructions#urshift() */
    public void urshift() {
        if( instructions != null ) instructions. urshift();
    }
}
