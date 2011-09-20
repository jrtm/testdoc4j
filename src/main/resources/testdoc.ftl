<#-- include "/header.ftl" -->

<#if testplans?exists>
<#list testplans as testplan>
    <#if testplan.title?exists>
      <h2>${testplan.title}</h2>
    <#else>
      <h2 style="color:red;">(No title)</h2>
    </#if>
    <#if testplan.className?exists>
      <p><em>Class: ${testplan.className}</em></p>
    </#if>

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
        
        <tr valign="top">
           <td<#if (test.getTasks()?exists)><#if (test.getTasks()?size > 1)> rowspan="${test.checksCount}"</#if></#if> >
             ${test.number}
           </td>
           <td<#if (test.getTasks()?exists)><#if (test.getTasks()?size > 1)> rowspan="${test.checksCount}"</#if></#if> > 
             <#if test.title?exists>
               ${test.title}
             <#else>
               &nbsp;
             </#if>
           </td>
           
           
           <#if test.getTasks()?exists> 
             <#list test.getTasks() as task>
             <#if (test.getTasks()?size > 1 && !(test.getTasks()?first == task) )>
        <tr> 
             </#if>     
             <td<#if (task.checks?size > 1)> rowspan="${task.checks?size}"</#if> >  
               ${task.title}
             </td>  
                 <#if task.getChecks()?exists>
                   <#list task.getChecks() as check>
                   <#if (task.checks?size > 1 && !(task.getChecks()?first == check))>
        <tr>       
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
    <tbody>
    </table>
    </#list>

    </#if>

<#-- include "/footer.ftl" -->