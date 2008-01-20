package {
    
    import flash.utils.Proxy;
    import flash.utils.flash_proxy;
    
    public dynamic class MyProxy extends Proxy
    {
        override flash_proxy function getProperty(name:*):*
        {
            if( name == "value" ) return 23; 
        
            return "a string value";
        }
        
        override flash_proxy function hasProperty(name:*):Boolean
        {
            if( name == "value" ) return true;         
            if( name == "foo" ) return true;         
            return false;   
        }       
    }
}