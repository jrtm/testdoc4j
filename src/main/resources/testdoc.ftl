<#-- include "/header.ftl" -->

<#if testplans?exists>

<ol>
<#list testplans as testplan>
    <#if testplan.title?? && testplan.getTests()?? && (testplan.getTests()?size >0) >
        <li>
            <a href="#doc-${testplan.sortOrder}"><b>${testplan.title}</b></a>
            <#if testplan.clazz??> <em>(${testplan.clazz.getSimpleName()})</em></#if>
        </li>
    </#if>
</#list>
</ol>


<#list testplans as testplan>
<div id="doc-${testplan.sortOrder}">
    <#if testplan.title?exists>
      <h2>${testplan.title}</h2>
    <#else>
      <h2>(No title)</h2>
    </#if>
    <#if testplan.clazz??>
      <p><em>Class: ${testplan.clazz.getName()}</em></p>
    </#if>
    
    <p class="test-description">
        <#if testplan.description?exists>
        ${testplan.description}
        </#if>
    </p>

    <#assign box_id=1>
    
    <#assign row_class="a">
        
    <table cellpadding="3" border="0" class="bodyTable">
    <tbody>
    <tr align="left" class="a">
       <th>No.</th>
       <th>Test</th>
       <th>Action</th>
       <th>Desired result</th>
    </tr>

    <#if testplan.getTests()?exists>
        <#list testplan.getTests() as test>
        
        
        <#if row_class =="a">
          <#assign row_class="b">
        <#else>
          <#assign row_class="a">
        </#if>
        
        
        <tr valign="top" <#if (test.isImplemented())> style="color:grey;" </#if> class="${row_class}" id="doc-${testplan.sortOrder}-${test.number}">
           <td<#if (test.getTasks()?exists)><#if (test.getTasks()?size > 1)> rowspan="${test.checksCount}"</#if></#if> >
             ${test.number}
           </td>
           <td<#if (test.getTasks()?exists)><#if (test.getTasks()?size > 1)> rowspan="${test.checksCount}"</#if></#if> > 
             <#if test.title?exists>
               ${test.title}
               <#if (test.isImplemented() == false)>
                 (Not implemented)
               </#if>
             <#else>
               &nbsp;
             </#if>
           </td>
           
           
           <#if test.getTasks()?exists> 
             <#list test.getTasks() as task>
             <#if (test.getTasks()?size > 1 && !(test.getTasks()?first == task) )>
        <tr class="${row_class}" <#if (test.isImplemented() == false)> style="color:grey;" </#if>> 
             </#if>     
             <td<#if (task.checks?size > 1)> rowspan="${task.checks?size}"</#if> >  
               ${task.title}
             </td>  
                 <#if task.getChecks()?exists>
                   <#list task.getChecks() as check>
                   <#if (task.checks?size > 1 && !(task.getChecks()?first == check))>
        <tr class="${row_class}" <#if (test.isImplemented() == false)> style="color:grey;" </#if>>       
                   </#if>
                                  
             <td valign="top">
                ${check}
             </td>
        </tr>              
        
                   </#list>
                 <#else>
                   <td>&nbsp;</td>
        </tr>
                 </#if>
               
             </#list>
           <#else>
             <td>&nbsp;</td>
             <td>&nbsp;</td>
        </tr>     
           </#if>
           
        
        </#list>
    <#else>
      <tr><td colspan="4" style="color:red">(No tests)</td></tr>
        
        
    </#if>
    </tbody>
    </table>
</div>
</#list>
</#if>

<#-- include "/footer.ftl" -->