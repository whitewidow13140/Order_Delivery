import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository repo;
    private final JmsTemplate jmsTemplate;

    public Order create(Order order) {
        Order saved = repo.save(order);
        OrderCreatedEvent evt = OrderCreatedEvent.builder()
                .orderId(saved.getId())
                .item(saved.getItem())
                .quantity(saved.getQuantity())
                .createdAt(saved.getCreatedAt())
                .build();
        try {
            jmsTemplate.convertAndSend("queue.orders.new", evt);
        } catch (Exception e) {
            log.error("JMS send failed, order created but event NOT sent", e);
        }
        return saved;
    }

    public List<Order> all() { return repo.findAll(); }
}
