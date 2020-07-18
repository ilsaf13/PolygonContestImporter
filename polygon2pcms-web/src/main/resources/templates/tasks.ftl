<html>
    <table>
        <tr>
            <th>Id</th>
            <th>Status</th>
            <th>Log</th>
        </tr>
        <#list tasks as task>
            <tr>
                <td>${task.id}</td>
                <td>${task.status}</td>
                <td><a href="showlog/${task.logfile.getName()}">Log</a></td>
            </tr>
        </#list>
    </table>
</html>
