package kopo.poly.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.repository.NoticeRepository;
import kopo.poly.repository.entity.NoticeEntity;
import kopo.poly.repository.entity.QNoticeEntity;
import kopo.poly.repository.entity.QUserInfoEntity;
import kopo.poly.service.INoticeService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeService implements INoticeService {

    // RequiredArgsConstructor 어노테이션으로 생성자를 자동 생성함
    // noticeRepository 변수에 이미 메모리에 올라간 NoticeRepository 객체를 넣어줌
    // 예전에는 autowired 어노테이션를 통해 설정했었지만, 이젠 생성자를 통해 객체 주입함
    private final NoticeRepository noticeRepository;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<NoticeDTO> getNoticeList() {

        log.info(this.getClass().getName() + ".getNoticeList Start!");

        // 공지사항 전체 리스트 조회하기
        List<NoticeEntity> rList = noticeRepository.getListFetchJoin();

        List<NoticeDTO> nList = new ArrayList<>();

        rList.forEach(e -> {
                    NoticeDTO rDTO = NoticeDTO.builder().
                            noticeSeq(e.getNoticeSeq()).title(e.getTitle()).noticeYn(e.getNoticeYn())
                            .readCnt(e.getReadCnt()).userId(e.getUserId())
                            .userName(e.getUserInfo().getUserName()).build(); // 회원이름

                    nList.add(rDTO);
                }
        );

        log.info(this.getClass().getName() + ".getNoticeList End!");

        return nList;
    }

    @Transactional
    @Override
    public NoticeDTO getNoticeInfo(NoticeDTO pDTO, boolean type) {

        log.info(this.getClass().getName() + ".getNoticeInfo Start!");

        if (type) {
            // 조회수 증가하기
            int res = noticeRepository.updateReadCnt(pDTO.noticeSeq());

            // 조회수 증가 성공여부 체크
            log.info("res : " + res);
        }

        // 공지사항 상세내역 가져오기
        NoticeEntity rEntity = noticeRepository.findByNoticeSeq(pDTO.noticeSeq());

        NoticeDTO rDTO = NoticeDTO.builder().noticeSeq(rEntity.getNoticeSeq())
                .title(rEntity.getTitle())
                .noticeYn(rEntity.getNoticeYn())
                .regDt(rEntity.getRegDt())
                .userId(rEntity.getUserId())
                .readCnt(rEntity.getReadCnt())
                .contents(rEntity.getContents()).build();

        log.info(this.getClass().getName() + ".getNoticeInfo End!");

        return rDTO;
    }

    @Transactional
    @Override
    public void updateNoticeInfo(NoticeDTO pDTO) {

        log.info(this.getClass().getName() + ".updateNoticeInfo Start!");

        Long noticeSeq = pDTO.noticeSeq();

        String title = CmmUtil.nvl(pDTO.title());
        String noticeYn = CmmUtil.nvl(pDTO.noticeYn());
        String contents = CmmUtil.nvl(pDTO.contents());
        String userId = CmmUtil.nvl(pDTO.userId());

        log.info("noticeSeq : " + noticeSeq);
        log.info("title : " + title);
        log.info("noticeYn : " + noticeYn);
        log.info("contents : " + contents);
        log.info("userId : " + userId);

        // 현재 공지사항 조회수 가져오기
        NoticeEntity rEntity = noticeRepository.findByNoticeSeq(noticeSeq);

        // 수정할 값들을 빌더를 통해 엔티티에 저장하기
        NoticeEntity pEntity = NoticeEntity.builder()
                .noticeSeq(noticeSeq).title(title).noticeYn(noticeYn).contents(contents).userId(userId)
                .readCnt(rEntity.getReadCnt())
                .build();

        // 데이터 수정하기
        noticeRepository.save(pEntity);

        log.info(this.getClass().getName() + ".updateNoticeInfo End!");

    }

    @Override
    public void deleteNoticeInfo(NoticeDTO pDTO) {

        log.info(this.getClass().getName() + ".deleteNoticeInfo Start!");

        Long noticeSeq = pDTO.noticeSeq();

        log.info("noticeSeq : " + noticeSeq);

        // 데이터 수정하기
        noticeRepository.deleteById(noticeSeq);


        log.info(this.getClass().getName() + ".deleteNoticeInfo End!");
    }

    @Override
    public void insertNoticeInfo(NoticeDTO pDTO) {

        log.info(this.getClass().getName() + ".InsertNoticeInfo Start!");

        String title = CmmUtil.nvl(pDTO.title());
        String noticeYn = CmmUtil.nvl(pDTO.noticeYn());
        String contents = CmmUtil.nvl(pDTO.contents());
        String userId = CmmUtil.nvl(pDTO.userId());

        log.info("title : " + title);
        log.info("noticeYn : " + noticeYn);
        log.info("contents : " + contents);
        log.info("userId : " + userId);

        // 공지사항 저장을 위해서는 PK 값은 빌더에 추가하지 않는다.
        // JPA에 자동 증가 설정을 해놨음
        NoticeEntity pEntity = NoticeEntity.builder()
                .title(title).noticeYn(noticeYn).contents(contents).userId(userId).readCnt(0L)
                .regId(userId).regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .chgId(userId).chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .build();

        // 공지사항 저장하기
        noticeRepository.save(pEntity);

        log.info(this.getClass().getName() + ".InsertNoticeInfo End!");

    }

    /**
     * 조회 로직은 Transaction 사용하지 않아도 됨
     * 그러나 영속성 처리 및 Lazy Loading 문제 해결을 위해 Transaction 사용함
     */
    @Transactional
    @Override
    public List<NoticeDTO> getNoticeListForQueryDSL() {

        log.info(this.getClass().getName() + ".getNoticeListForQueryDSL Start!");

        // QueryDSL 라이브러리를 추가하면, JPA 엔티티들은 Q붙여서 QueryDSL에서 처리가능한 객체를 생성함
        // 예 : NoticeEntity -> QNoticeEntity 객체 생성
        QNoticeEntity ne = QNoticeEntity.noticeEntity;
        QUserInfoEntity ue = QUserInfoEntity.userInfoEntity;

        // 공지사항 전체 리스트 조회하기
        List<NoticeEntity> rList = queryFactory
                .selectFrom(ne) // 조회할 Entity 및 항목 정의
                .join(ne.userInfo, ue) // Inner Joint 적용
                // 공지사항은 위로, 공지사항이 아닌 글들은 아래로 정렬한 뒤, 글 순번이 큰 순서대로 정렬
                .orderBy(ne.noticeYn.desc(), ne.noticeSeq.desc())
                .fetch(); // 결과를 리스트 구조로 반환하기

        List<NoticeDTO> nList = new ArrayList<>();

        rList.forEach(e -> {
                    NoticeDTO rDTO = NoticeDTO.builder().
                            noticeSeq(e.getNoticeSeq()).title(e.getTitle()).noticeYn(e.getNoticeYn())
                            .readCnt(e.getReadCnt()).userId(e.getUserId())
                            .userName(e.getUserInfo().getUserName()).build(); // 회원이름

                    nList.add(rDTO);
                }
        );

        log.info(this.getClass().getName() + ".getNoticeListForQueryDSL End!");

        return nList;
    }

    @Transactional
    @Override
    public NoticeDTO getNoticeInfoForQueryDSL(NoticeDTO pDTO, boolean type) throws Exception {

        log.info(this.getClass().getName() + ".getNoticeInfoForQueryDSL Start!");

        if (type) {
            // 조회수 증가하기
            int res = noticeRepository.updateReadCnt(pDTO.noticeSeq());

            // 조회수 증가 성공여부 체크
            log.info("res : " + res);
        }

        // QueryDSL 라이브러리를 추가하면, JPA 엔티티들은 Q붙여서 QueryDSL에서 처리가능한 객체를 생성함
        // 예 : NoticeEntity -> QNoticeEntity 객체 생성
        QNoticeEntity ne = QNoticeEntity.noticeEntity;

        // 공지사항 상세내역 가져오기
        // 공지사항 전체 리스트 조회하기
        NoticeEntity rEntity = queryFactory
                .selectFrom(ne) // 조회할 Entity 및 항목 정의
                .where(ne.noticeSeq.eq(pDTO.noticeSeq())) // Where 조건 정의 : where notice_seq = 10
                .fetchOne(); // 결과를 리스트 구조로 반환하기

        NoticeDTO rDTO = NoticeDTO.builder().noticeSeq(rEntity.getNoticeSeq())
                .title(rEntity.getTitle())
                .noticeYn(rEntity.getNoticeYn())
                .regDt(rEntity.getRegDt())
                .userId(rEntity.getUserId())
                .readCnt(rEntity.getReadCnt())
                .contents(rEntity.getContents()).build();

        log.info(this.getClass().getName() + ".getNoticeInfoForQueryDSL End!");

        return rDTO;
    }
}
