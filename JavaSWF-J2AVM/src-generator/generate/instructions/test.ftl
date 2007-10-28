<#list ops as op>
    ${op.name()}
    ${generator.dumpAnnos(op)}
</#list>