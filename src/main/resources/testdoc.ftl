<#-- include "/header.ftl" -->

<style type="text/css">
.implemented {
    color: grey !important;
}

.test-class {
    font-style: italic;
}
.test-name {
    font-weight: bold;
}

.bodyTable .test-header td {
    padding: 5px;
}
.task {
    font-family: Verdana, Helvetica, Arial, sans-serif;
    font-size: 13px;
}
.task ul{
    padding-left: 20px;
}
.task-num {
    font-size: 80%;
}
.bodyTable .task ul, .bodyTable .task td {
    margin: 1px;
}
.task .single-element {
    padding-left: 5px;
}
.no-tasks {
    font-style: italic;
}
</style>

<#macro optionalList elements>
    <#if elements?size gt 1>
        <ul>
        <#list elements as element>
            <li>${element!}</li>
        </#list>
        </ul>
    <#elseif elements?size == 1>
        <span class="single-element">${elements[0]!}</span>
    </#if>
</#macro>

<#if !testplans?exists || testplans?size == 0>
    <p class="no tasks">No tests found</p>
<#else>

<#-- Table of Contents -->
<ol>
<#list testplans as testplan>
    <li>
        <a href="#doc-${testplan.sortOrder}"><span class="test-name">${testplan.title!'(No title)'}</span></a>
        <span class="test-class">(${(testplan.clazz!).simpleName!'no class'})</span>
    </li>
</#list>
</ol>

<#list testplans as testplan>
    <div id="doc-${testplan.sortOrder}">
        <div class="test-plan-header">
            <h2>${testplan.title!'(No title)'}</h2>
            
            <p class="test-class">Class: ${(testplan.clazz!).name!'N/A'}</p>
            <p class="test-description">${testplan.description!}</p>
        </div>
        
        <table cellpadding="3" border="0" class="bodyTable">
            <thead>
                <tr align="left" class="a">
                    <th>No.</th>
                    <th>Action</th>
                    <th>Desired result</th>
                </tr>
            </thead>
            <tbody>
                <#list testplan.tests as test>
                    <#assign row_class = (test_index % 2 == 0)?string('a', 'b')>
                    <#assign test_link = "doc-${testplan.sortOrder}-${test_index + 1}">
                    
                    <tr id="${test_link}" class="a test-header <#if test.implemented>implemented</#if>">
                        <td><a href="#${test_link}">${test_index + 1}</a></td>
                        <td colspan="2">
                            ${test.title!}
                            <#if test.implemented>
                                <i>(Automated)</i>
                            </#if>
                        </td>
                    </tr>
                    
                    <#if test.tasks?? && test.tasks?size gt 0>
                        <#list test.tasks as task>
                            <tr class="b task <#if test.implemented>implemented</#if>" id="${test_link}-${task_index + 1}">
                                <td class="task-num"><a href="#${test_link}-${task_index + 1}">${test_index + 1}.${task_index + 1}</a></td>
                                <td class="task-steps"><@optionalList task.steps /></td>
                                <td class="task-checks"><@optionalList task.checks /></td>
                            </tr>
                        </#list>
                    <#else>
                        <tr class="b task">
                            <td class="task-num">&nbsp;</td>
                            <td class="no-tasks" colspan="2">No tasks</td>
                        </tr>
                    </#if>
                </#list>
            </tbody>
        </table>
    </div>
</#list>
</#if>

<#-- include "/footer.ftl" -->