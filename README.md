# otus-pw
OTUS Project Work

# Описание
Для реализации распределённой транзакции использовался паттерн Сага с хореографией.
- Заказ создаётся в микросервисе Order Service.
- Далее отправляется сообщение в Storage Service
- В Storage Service резервируется заказ
- Далее отправляется сообщение в Billing Service
- В Billing Service проводится платёж
- Далее отправляется сообщение в Delivery Service

| Микросервис         | Метод                                  | Компенсирующий метод |
|---------------------|----------------------------------------| -------------------- |
| Order Service       | createOrder                            | cancelOrder          |
| Storage Service     | reserveProduct                         | unreserveProduct     |
| **Billing Service** | **makePayment**<br/>(поворотная точка) | -                    |
| Delivery Service    | startDelivery                          | -                    |

При ошибке в Billing Service и Storage Service отменяется резерв и у заказа ставится статус CANCELED.
При успехе, когда дошло до доставки, у заказа ставится статус COMPLETED.

В тестах постмана:
1. Создание успешного платежа
2. Получение этого платежа (будет видно статус COMPLETED)
3. Создание платежа, где будет ошибка в Billing Service
4. Получение этого платежа (будет видно статус CANCELED)

# Установка
Устанавливать в дефолтный неймспейс
```
helm install otuspw otuspw-chart
```

# Удаление:
```
helm uninstall otuspw
kubectl delete pvc data-otuspw-postgresql-0
```
