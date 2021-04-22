<?php
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Slim\Factory\AppFactory;

require DIR . '/vendor/autoload.php';


$app = AppFactory::create();
$app->setBasePath("/API");

$app->addRoutingMiddleware();

$errorMiddleware = $app->addErrorMiddleware(true, true, true);
$app->get('/', function (Request $request, Response $response, $args) {
    $response->getBody()->write('Hello world!');
    return $response;
});

require_once('./api/problemas.php');
require_once('./api/utilizadores.php');


$app->run();
