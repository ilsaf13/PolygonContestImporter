<html>

<#if createdID??>
    <div>You created a task with id = ${createdID}</div>
</#if>

<div>
    <a href="tasks">All tasks</a>
</div>

<div>
    Import contest:
</div>
<div>
    <form method="post">
        <label>
            <input type="text" name="uid" placeholder="Enter contest uid"/>
        </label>
        <label>
            <select id="contestType" name="contestType">
                <option value="icpc">ICPC</option>
                <option value="ioi">IOI</option>
            </select>
        </label>
        <label>
            <select id="language" name="language">
                <option value="russian">Russian</option>
                <option value="english">English</option>
            </select>
        </label>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" name="Import">Import</button>
    </form>
</div>
</html>