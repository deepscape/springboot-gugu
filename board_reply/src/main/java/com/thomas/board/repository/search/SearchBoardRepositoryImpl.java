package com.thomas.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.thomas.board.entity.Board;
import com.thomas.board.entity.QBoard;
import com.thomas.board.entity.QMember;
import com.thomas.board.entity.QReply;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Board search1() {

        log.info("search1...");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        // jpqlQuery.select(board).where(board.bno.eq(20L));
        // jpqlQuery.select(board, member.email, reply.count()).groupBy(board);

        // 정해진 엔티티 객체 단위가 아니라, 각각의 데이터를 추출하는 경우에는 Tuple 이라는 객체 이용
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
        tuple.groupBy(board);

        log.info("-----------------------");
        // log.info(jpqlQuery);
        log.info(tuple);
        log.info("-----------------------");

        // List<Board> result = jpqlQuery.fetch();
        List<Tuple> result = tuple.fetch();
        log.info(result);

        return null;
    }

    // DTO 를 가능하면 Repository 영역에서 다루지 않음
    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

        log.info("searchPage...");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        // select b, w, count(r) from board b
        // left join b.writer w left join reply r on r.board = b
        // board 관점에서 writer 는 바로 조인되지만, reply 경우는 on 조건이 필요하다. manyToOne 방향에 따른 차이
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);

        booleanBuilder.and(expression);

        if(type != null) {
            String[] typeArr = type.split("");

            // 검색 조건을 작성하기
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t : typeArr) {
                switch (t) {
                    case "t":
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(board.content.contains(keyword));
                        break;
                }   // switch end
            }   // for end
            booleanBuilder.and(conditionBuilder);
        }   // if end

        tuple.where(booleanBuilder);

        // order by
        Sort sort = pageable.getSort();

        // tuple.orderBy(board.bno.desc());

        // param 으로 전달받는 Pageable 의 Sort 객체는 N개로 연결될 수 있다.
        // JPQL 에서는 Sort 객체를 지원하지 않음    ->   JPQLQuery 의 orderBy() 의 파라미터로 OrderSpecifier 를 사용
        sort.stream().forEach(order -> {
            // 정렬 관련 정보
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            // Sort 객체 속성(bno, title) 등은 PathBuilder 사용 / "board" 는 JPQLQuery 를 생성할 떄 변수명과 동일
            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");

            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        tuple.groupBy(board);

        // page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();
        log.info(result);

        long count = tuple.fetchCount();
        log.info("COUNT: " + count);

        // searchPage() 의 return type 은 Page<Object[]> 타입이므로 Page 타입 객체를 생성해야 한다.
        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, count);
    }

}