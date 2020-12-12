<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <title>Работа мечты</title>
</head>
<body>
<div class="container pt-3">
    <div class="row">
        <ul class="nav">
            <li class="nav-item">
                <a class="nav-link" href='<c:url value="/posts.do"/>'>Вакансии</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href='<c:url value="/candidates.do"/>'>Кандидаты</a>
            </li>
            <li>
                <a class="nav-link" href='<c:url value="/post/edit.do"/>'>Добавить вакансию</a>
            </li>
            <li>
                <a class="nav-link" href='<c:url value="/candidate/edit.do"/>'>Добавить кандидата</a>
            </li>
            <li>
                <a class="nav-link" href='<c:url value="/cities.jsp"/>'>Города</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href='<c:url value="/login.jsp"/>'>Войти</a>
            </li>
        </ul>
    </div>
    <div class="row">
        <div class="col">
        <div class="card">
            <div class="card-header">
                Города
            </div>
            <div class="card-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col">id</th>
                        <th scope="col">Наименование</th>
                    </tr>
                    </thead>
                    <tbody id="cityTable">
                    <c:forEach items="${cities}" var="city">
                        <tr>
                            <td>${city.id}</td>
                            <td>${city.name}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        </div>
        <div class="col">
            <div class="card">
                <div class="card-header">
                    Добавить город
                </div>
                <div class="card-body">
                    <label> Введите наименование:
                        <input type="text" id="new_city" name="city_name">
                    </label>
                    <button class="btn btn-success" id="add_city">Добавить</button>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="<c:url value="/js/cities.js"/>"></script>
</body>
</html>