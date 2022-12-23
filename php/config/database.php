<?php

class database
{
    // укажите свои учетные данные базы данных
    private $host = "localhost";
    private $db_name = "gotowork_db";
    private $username = "root";
    private $password = "";
    public $connection;

    // получаем соединение с БД
    public function getConnection()
    {
        $this->connection = null;

        try {
            $this->connection = new PDO("mysql:host=" . $this->host . ";dbname=" . $this->db_name, $this->username, $this->password);
            $this->connection->exec("set names utf8");
        } catch (PDOException $exception) {
            echo "Ошибка подключения: " . $exception->getMessage();
        }

        return $this->connection;
    }
}