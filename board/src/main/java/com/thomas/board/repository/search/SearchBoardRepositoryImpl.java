package com.thomas.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.thomas.board.entity.Board;
import com.thomas.board.entity.QBoard;
import com.thomas.board.entity.QMember;
import com.thomas.board.entity.QReply;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

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
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());

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
        tuple.groupBy(board);

        List<Tuple> result = tuple.fetch();
        log.info(result);

        return null;
    }

}
