<p>Hello world ${params.name}</p>
<#if hasParameter("foo")>
   <hr>Has FOO
   ${server.stop()}
</#if>
<hr>
${rootUrl}
<br>
${request.requestURL}
<br>
${defaultAttribute("index",1)}
${setAttribute("index",attribute("index")+1)}
${attribute("index")}