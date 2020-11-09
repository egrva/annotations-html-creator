<form method=${method} action=${actions}>
    <#list inputs as input>
        <input type=${input.type} name=${input.name} placeholder=${input.placeholder}>
    </#list>
</form>